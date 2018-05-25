(ns similar-set.core)

;; ANDY: this is terrible. re-write!!
;;;;;;;;;;;;;section;;;;;;;;;;;;;;;;;
(comment "Basic set operations with a supplied function used to establish
          whether an element is a member of the set")

(defn- switch [f] = (not f))

(defn- similar-set-elem-in
  "check whether an element (s) can be matched in a
  set (s1) with the supplied predicate function (f)"
   [s s2 f g]
    (if (empty? s2)
      (g false)
      (if (f s (first s2))
        (g true)
        (similar-set-elem-in s (rest s2) f g))))

(defn intersection
  "S1 ∩ S2. Intersection of two sets, s1 & s2 using supplied
  matching predicate function f"
  ([s1 s2 f] (intersection s1 s2 f nil))
  ([s1 s2 f acc]
   (if (not (empty? s1))
     (if (similar-set-elem-in (first s1) s2 f identity)
       (intersection (rest s1) s2 f (cons (first s1) acc))
       (intersection (rest s1) s2 f acc))
     (set acc))))

(defn difference
  "(S1 - S2). Returns a set that is the first set s1 without the elements
  of the second set s2, using the supplied matching predicate, f"
  ([s1 s2 f] (difference s1 s2 f nil))
  ([s1 s2 f acc]
   (if (not (empty? s1))
     (if (similar-set-elem-in (first s1) s2 f switch)
       (difference (rest s1) s2 f (cons (first s1) acc))
       (difference (rest s1) s2 f acc))
     (set acc))))

(defn union [s1 s2 f]
  "S1 ∪  S2. Union of two sets, s1 & s2 using supplied
  matching predicate function f"
  (set (concat (difference s2 s1 f) s1)))

(defn subset? [s1 s2 f]
  "Is the first set a subset of the second using supplied function as a matcher"
  (if (empty? (difference s1 s2 f)) true false))

(defn superset? [s1 s2 f]
  "Is the first set a superset of the second using supplied function as a matcher"
  (subset? s2 s1 f))

;;;;;;;;;;;;;section;;;;;;;;;;;;;;;;;
(comment "Grouping elements in the set into sub sets using supplied function")

(defn- subgroup [s1 s2 f]
  "Iterate over second set to establish whether is belongs in the first set"
  (loop [tgt s1 src s2 acc nil]
    (if (empty? src)
      [tgt acc]
      (if (subset? [(first src)] tgt f)
           (recur (cons (first src) tgt) (rest src) acc)
           (recur tgt (rest src) (cons (first src) acc))))))

(defn group [s f]
  "Split a set into looping using the comparator function"
  (loop [my-set s acc nil]
    (if (empty? my-set)
      (set (map set acc))
      (let [[x y] (subgroup [(first my-set)] (rest my-set) f)]
        (recur y (cons x acc))))))

;;;;;;;;;;;;;section;;;;;;;;;;;;;;;;;
(comment "More advanced set functions")

(defn symmetric-difference [s1 s2 f]
  "S1 ∆ S2 = (S1 - S2) ∪ (S2 - S1). Symmetric difference between two sets, i.e.
   The union of two sets minuse the intersection"
  (union (difference s1 s2 f) (difference s2 s1 f) f))

(defn cartesian-product [colls]
  "Cartesian product of a number of sets"
  (if (empty? colls)
    #{}
    (set (map set
     (for [x (first colls)
           more (cartesian-product (rest colls))]
       (cons x more))))))

(defn jaccard-index [s1 s2 f]
  "The Jaccard index or Jaccard similarity coefficient for two sets,
  using the supplied function as a matcher"
  (if (and (empty? s1) (empty? s2)) 1
    (/ (count (intersection s1 s2 f))
       (count (union s1 s2 f)))))

(defn jaccard-distance [s1 s2 f]
  "The Jaccard distance or Jaccard dissimilarity for two sets,
  using the supplied function as a matcher"
  (- 1 jaccard-index s1 s2 f))
