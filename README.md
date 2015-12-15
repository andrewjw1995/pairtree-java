# pairtree-java
A java implementation of the PairTree directory format

The Pairtree format is commonly used by digital libraries, which allows native operations, such as backup and restore, to work on the document database. Pairtree breaks a file name into pairs of letters, and creates a tree of folders using the pairs, with the file stored at the leaf.

The full specification can be found at:
https://confluence.ucop.edu/display/Curation/PairTree?preview=/14254128/16973838/PairtreeSpec.pdf
