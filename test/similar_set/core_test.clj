(ns similar-set.core-test
  (:require [clojure.test :refer :all]
            [similar-set.core :refer :all]
            [similar-set.levenshtein :refer :all]))

(deftest equality-intersection
  (testing "inetrsection supplied with = function works
            just like a standard intersection")
  (is (= (intersection (list 0 1 2 4 5) (list 1 2 3 4 7) =)
         #{1 4 2})))

(deftest equality-difference
  (testing "difference supplied with = function works
            just like a standard difference")
  (is (= (difference (list 0 1 2 4 5) (list 1 2 3 4 7) =)
         #{0 5})))

(deftest equality-union
  (testing "union supplied with = function works
            just like a standard union")
  (is (= (union (list 0 1 2 4 5) (list 1 2 3 4 7) =)
         #{0 7 1 4 3 2 5})))

;;set up some test strings
(def l1 (list "Jude" "Christina" "Anna" "Bridget" "Jack"))
(def l2 (list "Jude" "Christian" "Anne" "Bridgette" "Jackanory"))

(deftest equality-intersection-string
  (testing "intersection supplied with = function works
            just like a standard intersection")
  (is (= (intersection l1 l2 =)
         #{"Jude"})))

(deftest equality-difference-string
  (testing "difference supplied with = function works
            just like a standard difference")
  (is (= (difference l1 l2 =)
         #{"Christina" "Bridget" "Anna" "Jack"})))

(deftest equality-union-string
  (testing "union supplied with = function works
            just like a standard union")
  (is (= (union l1 l2 =)
         #{"Jude" "Christina" "Bridgette" "Bridget" "Anne" "Jackanory" "Christian" "Anna" "Jack"})))


;;test with fuzzy levenshtein matching function
(defn lev-lt-3 [m n]
  (if (< (levenshtein m n) 3) true false))

(defn lev-lt-2 [m n]
  (if (< (levenshtein m n) 2) true false))

(deftest intersection-lev3
  (testing "intersection supplied with lev-lt-3 function works as expected")
  (is (= (intersection l1 l2 lev-lt-3)
         #{"Jude" "Christina" "Bridget" "Anna"})))

(deftest intersection-lev2
  (testing "intersection supplied with lev-lt-2 function works as expected")
  (is (= (intersection l1 l2 lev-lt-2)
         #{"Jude" "Anna"})))

(deftest difference-lev3
  (testing "difference supplied with lev-lt-3 function works as expected")
  (is (= (difference l1 l2 lev-lt-3)
         #{"Jack"})))

(deftest difference-lev2
  (testing "difference supplied with lev-lt-2 function works as expected")
  (is (= (difference l1 l2 lev-lt-2)
         #{"Christina" "Bridget" "Jack"})))

(deftest difference-lev2-variant
  (testing "difference supplied with lev-lt-2 function works as expected (variant on above)")
  (is (= (difference l2 l1 lev-lt-2)
         #{"Bridgette" "Jackanory" "Christian"})))

(deftest union-lev2
  (testing "union supplied with lev-lt-2 function works as expected")
  (is (= (union l1 l2 lev-lt-2)
         #{"Jude" "Christina" "Bridgette" "Bridget" "Jackanory" "Christian" "Anna" "Jack"})))

(deftest union-lev2-variant
  (testing "union supplied with lev-lt-2 function works as expected (variant on above")
  (is (= (union l2 l1 lev-lt-2)
          #{"Jude" "Christina" "Bridgette" "Bridget" "Anne" "Jackanory" "Christian" "Jack"})))

(deftest union-lev3
  (testing "union supplied with lev-lt-3 function works as expected")
  (is (= (union l1 l2 lev-lt-3)
         #{"Jude" "Christina" "Bridget" "Jackanory" "Anna" "Jack"})))

;;set up more test strings
(def l3 (list "Christina" "Jack" "Anna" "Bridget" "Jude" ))

(deftest ordering-intersection-lev3
  (testing "order of supplied sets makes no difference to similar set operations")
  (is (= (intersection l1 l2 lev-lt-3)
         (intersection l3 l2 lev-lt-3))))

(deftest grouping-lev3
  (testing "set grouping works as expected when lev-lt-3 function used as comparator")
  (is (= (group #{"Jade" "Sude" "Bridget" "Jack" "Ollyi" "Jubbbde"} lev-lt-3)
   #{#{"Ollyi"} #{"Jubbbde"} #{"Bridget"} #{"Sude" "Jade" "Jack"}})))


