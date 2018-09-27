Instructions for using DomainThesaurus, PhraseStructureList and ForbiddenWords

DomainThesaurus:

1. Create a DomainThesaurus.txt file (or use a provided file) and put it in the program folder
2.1 Inside the DomainThesaurus.txt, write all the words that should dominate their synonyms (i.e. "verify" to dominate "check")
2.2 All words should be separated by a comma (",")
3. In DomainThesaurus.java go to line 27 ("String dtfile = [...]") and change the url to your file location
4. You can now run the program

PhraseStructureList:

1. Create a PhraseStructureList.txt file (or use a provided file) and put it in the program folder
2.1 Before creating a list of PhraseStructures, please go to the PhraseStructureTypes Class for all abbreviations of types to use in a PhraseStructure
2.2 If you want to use a PhraseStructure, first type in "y, " (for Yes) to make it active
2.3. After that input all the abbreviations that are used in this PhraseStructure - all abbreviations must be separated by a comma + a space
3. In PhraseStructureList.java go to line 24 ("String tsvFile = [...]") and change the url to your file location
4. You can now run the program

ForbiddenWords:

1. Create a ForbiddenWords.txt file (or use a provided file) and put it in the program folder
2.1 Before inserting forbidden words into the file, please remember that at this juncture, only verbs are classified as forbiddenWords
2.2 Inside the ForbiddenWords.txt file, write all the words (= verbs) that should be seen as forbidden
2.3 All words (= verbs) should be separated by a comma (",")
3. In ForbiddenWords.java go to line 27 ("String dtFile = [...]") and change the url to your file location
4. You can now run the program 