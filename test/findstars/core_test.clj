(ns findstars.core-test
  (:require [clojure.test :refer :all]
            [findstars.core :refer :all]))

(deftest test-append-sorted-with-limit
  (testing "Make sure we get a new list with the additional item"
    (let [initial-vect [{:distance 10 :coord [0 0 10]}
                        {:distance 12 :coord [12 0 0]}]
          new-star {:distance 11 :coord [0 11 0]}
          limit 2
          appended (append-sorted initial-vect new-star limit)]
      (is (= limit (count appended)))
      (is (= (first initial-vect) (first appended)))
      (is (= (second appended) new-star)))))

(deftest test-append-sorted-higher-limit
  (testing "Make sure we get a new list with the additional item"
    (let [initial-vect [{:distance 10 :coord [0 0 10]}
                        {:distance 12 :coord [12 0 0]}]
          new-star {:distance 11 :coord [0 11 0]}
          limit 10
          appended (append-sorted initial-vect new-star limit)]
      (is (= 3 (count appended)))
      (is (= (nth appended 0) (first initial-vect)))
      (is (= (nth appended 1) new-star))
      (is (= (nth appended 2) (second initial-vect))))))

(deftest test-should-append-star-smaller-distance
  (testing "Do we get truthy when the new-star is smaller?"
    (let [initial-vect [{:distance 10 :coord [0 0 10]}
                        {:distance 12 :coord [12 0 0]}]
          new-star {:distance 11 :coord [0 11 0]}
          limit 2]
      (is (= true
             (should-append-star? initial-vect new-star limit))))))

(deftest test-should-append-star-high-limit
  (testing "Do we get truthy when the limit is higher?"
    (let [initial-vect [{:distance 10 :coord [0 0 10]}
                        {:distance 12 :coord [12 0 0]}]
          new-star {:distance 200 :coord [0 11 0]}
          limit 10]
      (is (= true
             (should-append-star? initial-vect new-star limit))))))

(deftest test-should-append-star-lower-limit
  (testing "Do we get falsy when the limit is lower?"
    (let [initial-vect [{:distance 10 :coord [0 0 10]}
                        {:distance 12 :coord [12 0 0]}]
          new-star {:distance 11 :coord [0 11 0]}
          limit 1]
      (is (= false
             (should-append-star? initial-vect new-star limit))))))

(deftest test-should-append-star-higher-distance
  (testing "Do we get falsy when the distance is higher?"
    (let [initial-vect [{:distance 10 :coord [0 0 10]}
                        {:distance 12 :coord [12 0 0]}]
          new-star {:distance 200 :coord [0 11 0]}
          limit 2]
      (is (= false
             (should-append-star? initial-vect new-star limit))))))

(deftest test-calc-distance-3d
  (testing "Does our 3d math hold up to be true?"
    (let [coord1 [0 0 0]
          coord2 [4 2 4]
          distance (calc-distance-3d  coord1 coord2)]
      (is (= 6.0 distance)))))

(deftest test-calc-distance-3d-diff-origin
  (testing "Does our 3d math hold up to be true?"
    (let [coord1 [2 2 2]
          coord2 [6 4 6]
          distance (calc-distance-3d  coord1 coord2)]
      (is (= 6.0 distance)))))

(deftest test-calc-distance-3d-negative-numbers
  (testing "Does our 3d math hold up to be true?"
    (let [coord1 [-2 -2 -2]
          coord2 [-6 -4 -6]
          distance (calc-distance-3d  coord1 coord2)]
      (is (= 6.0 distance)))))

(defn close-enough?
  "We're only testing, not likely to accidentally get something this close"
  [d1 d2]
  ;; could probably round instead but this works
  (< (- d2 d1) 0.0001))

(deftest test-find-nearest-k-stars
  (testing "Do we actually get the closest ones?"
    (let [origin [0 0 0]
          fname "resources/test50.csv"
          k 3
          nearest-stars (find-nearest-k-stars origin fname k)]
      (is (close-enough?
           5.0E-6
           (:distance (nth nearest-stars 0))))
      (is (close-enough?
           40.74980029
           (:distance (nth nearest-stars 1))))
      (is (close-enough?
           42.30119971
           (:distance (nth nearest-stars 2)))))))

