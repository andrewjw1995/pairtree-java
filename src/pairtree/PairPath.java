package pairtree;

import java.io.File;
import java.util.Arrays;

/**
 * Represents a path within a pairtree folder structure. All paths have exactly
 * one equivalent file, which is the deepest directory in the path. Paths can
 * exist with no child paths and no leaf nodes.
 */
public abstract class PairPath
{
	protected File file;
	protected File[] objects;
	
	/**
	 * Returns the file that this path represents. The file is garuanteed to be a
	 * directory.
	 * 
	 * @return the file that this path represents
	 */
	public File getFile()
	{
		return file;
	}
	
	/**
	 * Returns an array of all the leaves contained specifically by this PairPath.
	 * A file is considered a pairtree leaf if its name is three characters or more.
	 * @return the objects contained by this PairPath
	 */
	public abstract File[] getLeaves();
	
	/**
	 * Returns an array of all the PairPaths that extend through this PairPath.
	 * @return the PairPaths that extend through this PairPath
	 */
	public abstract PairPath[] getChildPaths();
	
	/**
	 * Returns the name of the path, as represented by the PairTree format, after cleansing.
	 * This method assumes that the PairTree only contains properly escaped names.
	 * @return the escaped name of the path
	 */
	public abstract String getEscapedName();
	
	/**
	 * Returns the name of the path, before it was converted to PairTree format.
	 * This method assumes that the PairTree only contains properly escaped names.
	 * @return the unescaped name of the path
	 */
	public String getUnescapedName()
	{
		return unescape(getEscapedName());
	}
	
	/**
	 * Returns whether the path contains leaves <b>at this level</b>. Does not recurse over
	 * child paths.
	 * @return true if the path contains leaves
	 */
	public boolean hasLeaves()
	{
		return getLeaves().length > 0;
	}
	
	/**
	 * Returns whether the path ends here or can be followed deeper through child paths.
	 * @return true if the path contains child paths
	 */
	public boolean hasChildPaths()
	{
		return getChildPaths().length > 0;
	}
	
	/**
	 * Attempts to create a PairPath from the given file.
	 * @param file
	 *         the source of the PairPath
	 * @return a new PairPath
	 * @throws RuntimeException if the file is not a directory, or has a name longer than two characters
	 */
	public PairPath fromFile(File file)
	{
		if (!file.isDirectory())
			throw new RuntimeException(file.getName() + " is not a directory");
		int length = file.getName().length();
		switch(length)
		{
		case 1: return new Morty(this, file);
		case 2: return new Shorty(this, file);
		default:
			throw new RuntimeException(file.getName() + " is not a valid pairtree directory name");
		}
	}
	
	/**
	 * Takes a filesystem friendly string, and maps it to its' original UTF-8 string,
	 * based on successive pairs of characters.
	 * 
	 * @param name
	 *         the escaped version of the string
	 * @return the original string before it was escaped
	 */
	public static String unescape(String name)
	{
		StringBuilder builder = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++)
		{
			char c = name.charAt(i);
			if (c == '^')
			{
				String hex = name.substring(i + 1, i + 3);
				c = (char) Integer.parseInt(hex, 16);
				i += 2;
			}
			else
			{
				switch(c)
				{
				case '=':
					c = '/';
					break;
				case '+':
					c = ':';
					break;
				case ',':
					c = '.';
					break;
				}
			}
			builder.append(c);
		}
		return builder.toString();
	}

	/**
	 * Takes an arbitrary UTF-8 string, and maps it into a filesystem friendly string,
	 * based on successive pairs of characters.
	 * 
	 * @param name
	 *         the string to be escaped
	 * @return the escaped version of the original string
	 */
	public static String escape(String name)
	{
		char[] special = new char[]{ '"', '*', '+', ',', '<', '=', '>', '?', '\\', '^', '|' };
		StringBuilder builder = new StringBuilder(name.length());
		for (char c : name.toCharArray())
		{
			if (c < 0x21 || c > 0x7e || Arrays.asList(special).contains(c))
			{
				builder.append('^');
				builder.append(Integer.toHexString((int)c));
			}
			else
			{
				switch(c)
				{
				case '/':
					builder.append('=');
					break;
				case ':':
					builder.append('+');
					break;
				case '.':
					builder.append(',');
					break;
				default:
					builder.append(c);
					break;
				}
			}
		}
		return builder.toString();
	}
}
