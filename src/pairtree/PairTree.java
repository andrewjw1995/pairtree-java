package pairtree;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents the root of a pairtree, following the rules of the official Pairtree
 * specification. This class allows for iterating over all the paths of the tree.
 * 
 * @see <a href="https://wiki.ucop.edu/display/Curation/PairTree?preview=/14254128/16973838/PairtreeSpec.pdf">
 *      	Official PairTree Specification
 *      </a>
 */
public class PairTree extends PairPath implements Iterable<PairPath>
{
	protected PairPath[] children;

	/**
	 * Attempts to create a PairTree using the given file. The file should
	 * be a directory called "pairtree_root", as per the official specification.
	 * If it is not a directory, a runtime exception will be thrown.
	 * 
	 * @param file
	 *         the root directory of the pairtree
	 * @throws RuntimeException if the file given is not a directory
	 */
	public PairTree(File file)
	{
		if (!file.isDirectory())
			throw new RuntimeException(file + " is not a directory");
		if (!file.getName().equals("pairtree_root"))
			System.err.println("Improperly formed pairtree (root is not \"pairtree_root\").");
		this.file = file;
	}

	@Override
	public File[] getLeaves()
	{
		if (objects == null)
		{
			File[] files = file.listFiles();
			List<File> list = new ArrayList<>();
			for (File f : files)
				if (f.getName().length() > 2)
				{
					list.add(f);
					if (!f.getName().equals("obj") || !f.isDirectory())
						System.err.println("Improperly encapsulated pairtree object " + f);
				}

			objects = new File[list.size()];
			objects = list.toArray(objects);
		}
		return objects;
	}

	@Override
	public PairPath[] getChildPaths()
	{
		if (children == null)
		{
			File[] files = file.listFiles();

			List<PairPath> list = new ArrayList<>();
			for (File f : files)
				if (f.getName().length() <= 2)
					if (f.isDirectory())
						list.add(fromFile(f));
					else
						System.err.println("Improperly encapsulated pairtree object " + f);

			children = new PairPath[list.size()];
			children = list.toArray(children);
		}
		return children;
	}

	@Override
	public String getEscapedName()
	{
		return "";
	}

	@Override
	public Iterator<PairPath> iterator()
	{
		return new PairTreeIterator(this);
	}

	/**
	 * Searches within the given directory for a sub-directory named
	 * "pairtree_root". Returns the first occurrence, or null if none are found.
	 * Returns null if scope is not a directory.
	 * 
	 * @param scope
	 *            the file or directory to search
	 * @return the root folder of the pairtree, if it exists, or null
	 */
	// TODO: Change from depth first search to breadth first
	public static File findRoot(File scope)
	{
		if (!scope.isDirectory())
			return null;

		if (scope.getName().equals("pairtree_root"))
			return scope;

		File[] children = scope.listFiles();
		File root = null;
		for (File child : children)
		{
			root = findRoot(child);
			if (root != null)
				return root;
		}

		return null;
	}
}
