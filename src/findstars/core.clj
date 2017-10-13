(ns findstars.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io])
  (:gen-class))

;; Assumes this spec still matches these values: https://github.com/astronexus/HYG-Database
(def COORD_INDEX_X 17)
(def COORD_INDEX_Y 18)
(def COORD_INDEX_Z 19)

(defn calc-distance-3d
  "Found the formula here: 
   https://www.varsitytutors.com/hotmath/hotmath_help/topics/distance-formula-in-3d"
  [[x1 y1 z1] [x2 y2 z2]]
  ;; could use squared distance instead of real distance but it had
  ;; no noticeable effect on performance
  (Math/sqrt (+ (Math/pow (- x2 x1) 2)
                (Math/pow (- y2 y1) 2)
                (Math/pow (- z2 z1) 2))))

(defn parse-coord
  "Grabs specific hardcoded columns out of a csv row"
  [row]
  (let [x (read-string (nth row COORD_INDEX_X))
        y (read-string (nth row COORD_INDEX_Y))
        z (read-string (nth row COORD_INDEX_Z))]
    [x y z]))

(defn append-sorted
  "Appends a value to a list, sorts the list, and limits the size"
  [vect to-append limit]
  (->> (conj vect to-append)
       (sort-by :distance)
       (take limit)))

(defn should-append-star?
  "Capture biz logic for when to append a star to our running list.
   Assumes a sorted list but the list can be larger than k"
  [current-stars new-star k]
  ;; either we don't have enough stars yet
  (or (< (count current-stars) k)
      ;; or the distance is less than our kth element
      (< (:distance new-star)
         (:distance (nth current-stars (- k 1))))))

(defn find-nearest-k-stars
  "Lazy opens and parses a file of HYG star data to find the closest k
   stars to the origin passed in."
  [origin fname k]
  (with-open [reader (io/reader fname)]
    (->> (csv/read-csv reader)
         (rest) ; skip the first header line
         (pmap parse-coord)
         (reduce (fn [cur-stars coord]
                   (let [distance (calc-distance-3d origin coord)
                         new-star {:coord coord :distance distance}]
                     (if (should-append-star? cur-stars new-star k)
                       (append-sorted cur-stars new-star k)
                       cur-stars)))
                 [] ; empty list default for the reduce function
                 ))))

(defn print-results
  "Prints out messages to the users about their results"
  [origin nearest-neighbors k]
  (println (str "Your nearest " k " stars from " origin " are: "))
  (mapv #(println (:coord %)) nearest-neighbors))

(defn -main
  "I parse the filename as a csv and find the k nearest stars"
  ([fname k]
   (-main fname k "0" "0" "0"))
  ([fname k x0 y0 z0]
   ;; convert numbers passed in from strings
   (let [origin [(read-string x0)
                 (read-string y0)
                 (read-string z0)]
         k (read-string k) 
         nearest-neighbors (find-nearest-k-stars origin fname k)]
     (print-results origin nearest-neighbors k)
     (System/exit 0))))
