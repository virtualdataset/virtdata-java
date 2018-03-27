package io.virtdata.stathelpers.aliasmethod;

import io.virtdata.api.ThreadSafeMapper;
import io.virtdata.stathelpers.EvProbD;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.DoubleToIntFunction;
import java.util.stream.Collectors;

/**
 * Uses the alias sampling method to encode and sample from discrete probabilities,
 * even over larger sets of data. This form requires a unit interval sample value
 * between 0.0 and 1.0. Assuming the maximal amount of memory is used for distinct
 * outcomes N, a memory buffer of N*16 bytes is required for this implementation,
 * requiring 32MB of memory for 1M entries. Not bad, eh?
 *
 * This sampler should be shared between threads, and will be by default, in order
 * to avoid many instances of a 32MB buffer on heap.
 */
@ThreadSafeMapper
public class AliasSampler implements DoubleToIntFunction {

    private ByteBuffer stats; // tuples of double,int,int (unfair coin, direct pointers to referents)
    private double slotCount; // The number of fair die-roll slotCount that contain unfair coin probabilities
    private static int _r0=0;
    private static int _r1=_r0+Double.BYTES;
    private static int _r2=_r1+Integer.BYTES;
    public static int RECORD_LEN = _r2 + Integer.BYTES; // Record size for the above.

    // for testing
    AliasSampler(ByteBuffer stats) {
        this.stats = stats;
        if ((stats.capacity()% RECORD_LEN)!=0) {
            throw new RuntimeException("Misaligned ByteBuffer size, must be a multiple of " + RECORD_LEN);
        }
        slotCount = (stats.capacity()/ RECORD_LEN);
    }

    public AliasSampler(List<EvProbD> events) {
        int size = events.size();
        double sumProbability = events.stream().mapToDouble(EvProbD::getProbability).sum();
        events = events.stream().map(e -> new EvProbD(e.getEventId(), (e.getProbability()/sumProbability)*size)).collect(Collectors.toList());

        Collections.sort(events,EvProbD.DESCENDING_PROBABILTY); // reverse probability
        LinkedList<EvProbD> llevents = new LinkedList<>(events);
//        int size = events.size()/2;

        ArrayList<Slot> slots = new ArrayList<>();

        //LinkedList<EvProbD> nextlist = new LinkedList<>(events);
        while (llevents.peekFirst()!=null) {
            EvProbD first = llevents.removeFirst();
            double remaining = first.getProbability();
            if (remaining < 1.0D) {
                throw new RuntimeException("first event must have an array-normalized probability greater than or equal to 1.0D");
            }
            while (remaining >= 1.0D && llevents.peekLast()!=null) {
                EvProbD last = llevents.removeLast();
                Slot slot = new Slot(first.getEventId(), last.getEventId(), last.getProbability());
                slots.add(slot);
                remaining -= (1.0D - last.getProbability());
            }
            if (remaining==1.0D || Math.abs(1.0D - remaining)<0.0000000001D) {
                slots.add(new Slot(first.getEventId(), first.getEventId(), 1.0D));
            } else if (remaining>0.0000000000001D) {
                first.setProbability(remaining);
                ListIterator<EvProbD> lit = llevents.listIterator();
                // This is the bottleneck for large data sets
                while (lit.hasNext() && lit.next().getProbability() > remaining) {
                }
                lit.add(first);
            } else {
                throw new RuntimeException("Is this an issue?");
            }
        }
        if (slots.size()!=size) {
            throw new RuntimeException("basis for average probability is incorrect, because only " + slots.size() + " slotCount of " + size + " were created.");
        }
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).rescale(i, i+1);
        }
        this.stats = ByteBuffer.allocate(slots.size()* RECORD_LEN);

        for (Slot slot : slots) {
            stats.putDouble(slot.botProb);
            stats.putInt(slot.botItx);
            stats.putInt(slot.topIdx);
        }
        stats.flip();
        this.slotCount = (stats.capacity()/ RECORD_LEN);

    }

    @Override
    public int applyAsInt(double value) {
        double fractionlPoint = value * slotCount;
        int offsetPoint = (int) fractionlPoint * RECORD_LEN;
        double divider = stats.getDouble(offsetPoint);
        int selector = offsetPoint+ (fractionlPoint>divider?_r2:_r1);
        int referentId = stats.getInt(selector);
        return referentId;
    }

    private static class Slot {
        public int topIdx;
        public int botItx;
        public double botProb;

        public Slot(int topIdx, int botItx, double botProb) {
            this.topIdx = topIdx;
            this.botItx = botItx;
            this.botProb = botProb;
        }

        public String toString() {
            return "top:" + topIdx + ", bot:" + botItx + ", botProb: " + botProb;
        }

        public Slot rescale(double min, double max) {
            botProb = (min + (botProb*(max-min)));
            return this;
        }
    }
}
