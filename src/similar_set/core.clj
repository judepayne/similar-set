(ns similar-set.core)

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
  "intersection of two sets, s1 & s2 using supplied
  matching predicate function f"
  ([s1 s2 f] (intersection s1 s2 f nil))
  ([s1 s2 f acc]
   (if (not (empty? s1))
     (if (similar-set-elem-in (first s1) s2 f identity)
       (intersection (rest s1) s2 f (cons (first s1) acc))
       (intersection (rest s1) s2 f acc))
     (set acc))))

(defn difference
  "returns a set that is the first set s1 without the elements
  of the second set s2, using the supplied matching predicate, f"
  ([s1 s2 f] (difference s1 s2 f nil))
  ([s1 s2 f acc]
   (if (not (empty? s1))
     (if (similar-set-elem-in (first s1) s2 f switch)
       (difference (rest s1) s2 f (cons (first s1) acc))
       (difference (rest s1) s2 f acc))
     (set acc))))

(defn union [s1 s2 f]
  "union of two sets, s1 & s2 using supplied
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
  "iterate over second set to establish whether is belongs in the first set"
  (loop [tgt s1 src s2 acc nil]
    (if (empty? src)
      [tgt acc]
      (if (subset? [(first src)] tgt f)
           (recur (cons (first src) tgt) (rest src) acc)
           (recur tgt (rest src) (cons (first src) acc))))))

(defn group [s f]
  "split a set into looping using the comparator function"
  (loop [my-set s acc nil]
    (if (empty? my-set)
      (set (map set acc))
      (let [[x y] (subgroup [(first my-set)] (rest my-set) f)]
        (recur y (cons x acc))))))
