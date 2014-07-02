(ns similar-set.levenshtein)

(declare levenshtein)

(defn levenshtein-raw
  "calculates the levenshtein distane between two strings"
  [a b]
  (let
      [len-a (. (into [] a) length)
       len-b (. (into [] b) length)
       cost (if (= (first a) (first b)) 0 1)]
    (if (or (= len-a 0) (= len-b 0))
      (+ len-a len-b)
      (min
       (+ (levenshtein (rest a) b) 1)
       (+ (levenshtein a (rest b)) 1)
       (+ (levenshtein (rest a) (rest b)) cost)))))

(def levenshtein (memoize levenshtein-raw))
