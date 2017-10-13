# findstars

Takes in a filename of HYG stars, reads, it, and finds the stars closest to your origin.

## Prereqs

Java 1.6, 1.7, or 1.8 on your $PATH

[Install Clojure](https://clojure.org/guides/getting_started)

[Install Leiningen](https://leiningen.org/)

## Testing it

`lein test` runs tests

## RUnning it

- Download the hyg data here: http://www.astronexus.com/files/downloads/hygdata_v3.csv.gz
- Uncompress and note the filename

`lein run hygdata_v3.csv k` will parse the given filename for the k nearest stars
`lein run hygdata_v3.csv k x0 y0 z0` will parse the given filename for the k nearest stars to an origin point in space defined by (x0 y0 z0)



