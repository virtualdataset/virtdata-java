package io.virtdata.docsys.metafs.fs.renderfs.api;


import java.util.LinkedList;

/**
 * Capture the layering order of renderable elements.
 * The inner most element is first, and parent elements follow.
 * This allows for simple iterable based rendering and combining.
 */
public class InToOutRenderingLayers {

    private LinkedList<Renderable> layers;

    public InToOutRenderingLayers addParent(Renderable parentLayer) {
        this.layers.add(parentLayer);
        return this;
    }

}
