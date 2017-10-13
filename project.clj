(defproject findstars "0.1.0"
  :description "Given a database of HYG stars, find the nearest neighbors"
  :url "http://github.com/whitespaceninja/findstars"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.csv "0.1.4"]]
  :main findstars.core
  :aot [findstars.core])
