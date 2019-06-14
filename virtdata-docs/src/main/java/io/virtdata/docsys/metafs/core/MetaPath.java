package io.virtdata.docsys.metafs.core;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A MetaPath represent a logical path for one of any MetaFS derivatives.
 * A MetaPath is meant to be filesystem provider agnostic in terms of the
 * path syntax. Internally, the MetaPath syntax sill defer to the
 * default FileSystem provider's syntax.
 */
public class MetaPath implements Path {
    protected final String[] path;
    private final MetaFS filesystem;
    private final boolean isAbsolute;

    public MetaPath(MetaFS metaFS, String initial, String... remaining) {
        this.filesystem = metaFS;

        isAbsolute = initial.startsWith(FileSystems.getDefault().getSeparator());
        if (isAbsolute) {
            initial = initial.substring(1);
        }

        //path = new String[remaining.length+1];
        StringBuilder sb = new StringBuilder();
        sb.append(initial);
        sb.append(FileSystems.getDefault().getSeparator());
        for (String s : remaining) {
            sb.append(s);
        }

        path = normalize(sb.toString().split(Pattern.quote(FileSystems.getDefault().getSeparator())));
    }

    public MetaPath(MetaFS metaFS, String[] components, boolean absolute) {
        this.filesystem = metaFS;
        this.path = components;
        this.isAbsolute = absolute;
    }

    private static String[] normalize(String[] components) {
        String[] target = new String[components.length];
        int targetidx = 0;
        String sep = FileSystems.getDefault().getSeparator();

        int levels = 0;
        for (int i = 0; i < components.length; i++) {
            if (i == 0 && components[i].equals(".")) {
                target[targetidx] = components[i];
                targetidx++;
            } else if (components[i].equals("..") && (targetidx > 0)) {
                targetidx--;
                target[targetidx] = null;
            } else if (i > 0 && components[i].equals(sep) && components[i - 1].equals(sep)) {
            } else if (components[i].isEmpty()) {
            } else {
                target[targetidx] = components[i];
                targetidx++;
            }
        }
        for (int backlevels = 0; backlevels < levels; backlevels++) {
            target[targetidx] = "..";
            targetidx++;
        }

        if (targetidx == components.length) {
            return components;
        } else {
            String[] sbuf = new String[targetidx];
            System.arraycopy(target, 0, sbuf, 0, targetidx);
            return sbuf;
        }
    }

    @Override
    public MetaFS getFileSystem() {
        return filesystem;
    }

    @Override
    public boolean isAbsolute() {
        return isAbsolute;
    }

    @Override
    public Path getRoot() {
        if (this.isAbsolute) {
            return new MetaPath(filesystem, FileSystems.getDefault().getSeparator());
        }
        return null;
    }

    @Override
    public Path getFileName() {
        if (path.length == 0) {
            return null;
        }
        return new MetaPath(filesystem, path[path.length - 1]);
    }

    @Override
    public Path getParent() {
        if (path.length==0) return null;
        String[] parentArray = new String[path.length - 1];
        System.arraycopy(path, 0, parentArray, 0, parentArray.length);
        return new MetaPath(filesystem, parentArray, isAbsolute);
    }

    @Override
    public int getNameCount() {
        return path.length;
    }

    @Override
    public Path getName(int index) {
        return new MetaPath(filesystem, path[index]);
    }

    @Override
    public Path subpath(int beginIndex, int endIndex) {
        if (beginIndex < 0 || endIndex > path.length - 1) {
            throw new InvalidParameterException("Index range must be within available path name count: " +
                    "0 < begin(" + beginIndex + ") <= end(" + endIndex + ") <= " + (path.length - 1) + " ?");
        }
        int len = (endIndex - beginIndex) + 1;
        String[] components = new String[len];
        System.arraycopy(path, beginIndex, components, 0, len);
        return new MetaPath(filesystem, components, false);
    }

    @Override
    public boolean startsWith(Path other) {
        if (path.length < other.getNameCount()) {
            return false;
        }
        if (getFileSystem() != other.getFileSystem()) {
            return false;
        }
        if (isAbsolute != other.isAbsolute()) {
            return false;
        }
        for (int i = 0; i < other.getNameCount(); i++) {
            if (!path[i].equals(other.getName(i).toString())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean endsWith(Path other) {
        if (other.isAbsolute()) {
            return false;
        }
        if (getNameCount() < other.getNameCount()) {
            return false;
        }
        for (int i = 0; i < other.getNameCount(); i++) {
            if (!path[path.length - i].equals(other.getName(other.getNameCount() - (i + 1)))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Path normalize() {
        String[] normalized = normalize(path);
        if (normalized != path) {
            return new MetaPath(filesystem, normalized, isAbsolute);
        }
        return this;
    }

    @Override
    public Path resolve(Path other) {
        MetaPath ometa = assertMetaPath(other);

        if (ometa.isAbsolute()) {
            return other;
        }
        if (ometa.path.length == 0) {
            return this;
        }

        String[] resolved = new String[path.length + ometa.path.length];
        System.arraycopy(path, 0, resolved, 0, path.length);
        System.arraycopy(ometa.path, 0, resolved, path.length, ometa.path.length);
        return new MetaPath(filesystem, resolved, isAbsolute);
    }

    @Override
    public Path relativize(Path other) {
        MetaPath mpath = assertMetaPath(other);
        if (isAbsolute() != other.isAbsolute()) {
            throw new IllegalArgumentException("Unable to relativize '" + other + "' against '" + this + "');");
        }

        int common_idx = 0;
        while (common_idx < path.length && path[common_idx].equals(mpath.path[common_idx])) {
//            sbuf[common_idx]=path[common_idx];
            common_idx++;
        }
        int back_idx = path.length - common_idx;
        String[] sbuf = new String[back_idx + (mpath.path.length - common_idx)];
        for (int updir = 0; updir < back_idx; updir++) {
            sbuf[updir] = "..";
        }

        System.arraycopy(mpath.path, common_idx, sbuf, back_idx, mpath.path.length - common_idx);

        return new MetaPath(filesystem, sbuf, false);
    }


    @Override
    public Path toAbsolutePath() {
        return getRoot().resolve(this);
    }

    @Override
    public Path toRealPath(LinkOption... options) throws IOException {
        return toAbsolutePath();
    }

    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
        return null;
    }

    @Override
    public int compareTo(Path other) {
        int thisSize = getNameCount();
        int thatSize = other.getNameCount();

        int commonIdx = Math.min(thisSize, thatSize);
        for (int i = 0; i < commonIdx; i++) {
            int diff = getName(i).compareTo(other.getName(i));
            if (diff != 0) {
                return diff;
            }
        }
        return Integer.compare(thisSize, thatSize);
    }

    //    private Path tmpSysPath() {
//        if (path.length>0) {
//            return FileSystems.getDefault().getPath(
//                    (isAbsolute() ? FileSystems.getDefault().getSeparator() : "") + path[0],
//                    Arrays.copyOfRange(path,1,path.length-1)
//            );
//        }
//        return File
//    }
    private Path tmpSysPath(MetaPath other) {
        return FileSystems.getDefault().getPath(
                (other.isAbsolute() ? FileSystems.getDefault().getSeparator() : "") + other.path[0],
                Arrays.copyOfRange(other.path, 1, other.path.length - 1)
        );
    }

    private void assertFilesystemOwnership(Path other) {
        if (other.getFileSystem() != filesystem) {
            throw new InvalidParameterException("This path is from a different filesystem.");
        }
        if (!(other instanceof MetaPath)) {
            throw new InvalidParameterException("This path is not of type MetaPath");
        }
    }

    private String joinedPath() {
        StringBuilder sb = new StringBuilder();
        if (isAbsolute()) {
            sb.append(FileSystems.getDefault().getSeparator());
        }
        for (String s : path) {
            sb.append(s);
            sb.append(FileSystems.getDefault().getSeparator());
        }
        try {
            if (sb.length() > 1) {
                sb.setLength(sb.length() - 1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return sb.toString();
    }


    @Override
    public String toString() {
        return (isAbsolute() ? FileSystems.getDefault().getSeparator() : "") + Arrays.stream(path).collect(Collectors.joining(getFileSystem().getSeparator()));
    }

    public MetaPath asRelativePath() {
        return new MetaPath(filesystem, path, false);
    }

    @Override
    public URI toUri() {
        try {
            String joinedpath = joinedPath();
            BasicFileAttributeView fav = filesystem.provider().getFileAttributeView(
                    this,
                    BasicFileAttributeView.class
            );

            if (!joinedpath.endsWith("/") && fav.readAttributes().isDirectory()) {
                joinedpath = joinedpath + "/";
            }
            return new URI(filesystem.provider().getScheme(), null, joinedpath, null);
        } catch (URISyntaxException e) {
            throw new InvalidParameterException("Unable to create URI from " + this + ": " + e.getInput());
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to read attributes for " + this.toString());
        }
    }

    private MetaPath assertMetaPath(Path other) {
        if (other instanceof MetaPath) {
            MetaPath metapath = (MetaPath) other;
            if (metapath.getFileSystem() != this.getFileSystem()) {
                throw new InvalidParameterException("Using paths from two different filesystems: (this) " + this.getFileSystem().toString() + " (other) " + other.getFileSystem().toString());
            }
            return metapath;
        } else {
            throw new InvalidParameterException("expected a MetaPath instance, got " + other.getClass().getCanonicalName() + " instead");
        }
    }

    /**
     * This Path implementation does not allow the caller to break out to File abstractions.
     *
     * @return null, signifying that there are no defined File semantics for a Path
     */
    @Override
    public File toFile() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetaPath paths = (MetaPath) o;

        if (isAbsolute != paths.isAbsolute) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(path, paths.path)) return false;
        return filesystem != null ? filesystem.equals(paths.filesystem) : paths.filesystem == null;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(path);
        result = 31 * result + (filesystem != null ? filesystem.hashCode() : 0);
        result = 31 * result + (isAbsolute ? 1 : 0);
        return result;
    }
}
