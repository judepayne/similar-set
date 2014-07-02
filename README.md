# similar-set

A clojure library for doing set operations using a supplied matching function
rather than an (implicit) equality test. Useful for fuzzy matches on Sets.
For example this is useful if you want to want match lists of names but they may be formatting
or spelling differences between the same name in two lists.

intersection, difference and union operations only are supplied


## Usage

add the following to your project.clj:

    [similar-set "0.1.0"]

then in your code file/s:

    (use 'similar-set.core)

intersection, difference and union operations on (two) sets using a
comparator/ matching function.
The matching function should take one member of each of the (two) sets
and return either true or false, if they match or not.


1) using = as the function for testing matching:


    (intersection (list 0 1 2 4 5) (list 1 2 3 4 7) = )  => #{1 2 4}
    (difference (list 0 1 2 4 5) (list 1 2 3 4 7) = )  => #{0 5}
    (union (list 0 1 2 4 5) (list 1 2 3 4 7) = ) => #{0 1 2 3 4 5 7}


So these operations work just the same as their namesakes in clojure.set

2) More usefully, Say you're using the levenshtein fuzzy string matching algorithm from
mihi-tr's fuzzy-string library (https://github.com/mihi-tr/fuzzy-string) to see
if two strings are similar enough to be assumed to be the same.


    (def l1 (list "Jude" "Christina" "Anna" "Bridget" "Jack"))
    (def l2 (list "Jude" "Christian" "Anne" "Bridgette" "Jackanory"))


    (defn lev-lt-2
      "This function returns true if the two supplied strings are within
      2 insertions, deletions or substitutions of each other"
      [m n]
      (if (< (levenshtein m n) 2) true false))


Now, let's try some sets operations using our 'fuzzy' matching function.


    (intersection l1 l2 lev-lt-2) => #{"Anna" "Jude"}
    (difference l1 l2 lev-lt-2) => #{"Bridget" "Christina" "Jack"}
    (union l1 l2 lev-lt-2) => #{"Anna" "Jackanory" "Bridget" "Jude" "Christian" "Bridgette" "Christina" "Jack"}

NOTE: Unlike set operations using = as the matching function, the order that
you supply the sets is important for union operations. e.g.

    (union l1 l2 lev-lt-2) => #{"Anna" "Jackanory" "Bridget" "Jude" "Christian" "Bridgette" "Christina" "Jack"}

Anna and Anne were matched as being the same but the value supplied comes from l1.

    (union l2 l1 lev-lt-2) => #{"Anne" "Jackanory" "Bridget" "Jude" "Christian" "Bridgette" "Christina" "Jack"}

whereas the value supplied comes from l2.

For matched but not necessarily identical values, the value supplied in the union
operation always come from the first supplied set.

Others functions included (using supplied function for matching):
`subset? [s1 s2 f]` ;;is s1 a subset of s2?
`superset? [s1 s2 f]` ;;is s1 a superset of s2?
`group [s1 f]`  ;;spilts a set into groups using function
`symmetric-difference [s1 s2 f]` ;;the union of two sets minus the intersection
`cartesian-products [s & more]` ;;cartesian-product of n sets
`jaccard-index [s1 s2 f]` ;;jaccard-index/ similarity of two sets, value between 0 & l1
`jaccard-distance [s1 s2 f]` ;;jaccard-distance/ dissimilarity between two sets, between 0 & 1

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
