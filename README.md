# so_long_ow2

Translate an ow2 wiki database into a set of markdown files, suitable for Jekyll.
To make my life complex and learn something new, let's do it in Clojure.

References:

* ow2 markup: http://ow2wiki.cvs.sourceforge.net/viewvc/ow2wiki/ow2wiki/DEVELOPERS/unitTest/html_parser_unitTest.php?revision=1.1&view=markup



## Installation

Download from http://example.com/FIXME.

## Usage

Run tests: evaluate namespaces manually and open an Instarepl and

(clojure.test/run-all-tests)



A good page to translate is:

http://astrecipes.net/index.php?from=60&q=AstRecipes/Using%20a%20HT-488%20with%20Asterisk



(spit "resources/Sample.md"
      (so-long-ow2.translate/translate-ow2
        (slurp "resources/SampleNode.txt")))




FIXME: explanation

    $ java -jar so_long_ow2-0.1.0-standalone.jar [args]


## SQL search queries

SELECT *
FROM ow2_nodes as N,
     ow2_texts as T
WHERE idArea = 4
  AND T.`idNode` = N.`idNode`
  AND T.`isValid` = 1




## Options

FIXME: listing of options this app accepts.

## Examples

...

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2015

Distributed under the MIT License.
