package pairtree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a path that ends with a single character. A Morty cannot contain
 * child paths.
 */
public class Morty extends PairPath
{
	protected PairPath parent;
	
	public Morty(PairPath parent, File file)
	{
		if (!file.isDirectory())
			throw new RuntimeException(file.getName() + " is not a directory");
		if (file.getName().length() != 1)
			throw new RuntimeException(file.getName() + " is not a morty (size 1) directory");
		this.file = file;
		this.parent = parent;
	}

	@Override
	public File[] getLeaves()
	{
		if (objects == null)
		{
			File[] files = file.listFiles();
			List<File> list = new ArrayList<File>();
			for (File f : files)
				if (f.getName().length() > 2)
					list.add(f);
				else
					System.err.println("Improperly encapsulated pairtree object " + f);
			
			objects = new File[list.size()];
			objects = list.toArray(objects);
		}
		return objects;
	}

	@Override
	public PairPath[] getChildPaths()
	{
		return new PairPath[0];
	}

	@Override
	public String getEscapedName()
	{
		return parent.getEscapedName() + file.getName();
	}
}
