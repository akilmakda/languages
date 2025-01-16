(ns run
  (:require [languages.benchmark :as benchmark])
  (:gen-class))

(set! *unchecked-math* :warn-on-boxed)

(definterface IFib
  (^long fib [^long n]))

(deftype Fibonacci []
  IFib
  (fib [_  n]
    (if (or (zero? n)
            (== 1 n))
      (long n)
      (long (+ (.fib _ (- n 1))
               (.fib _ (- n 2)))))))

(def ^:private ^Fibonacci fibonacci (Fibonacci.))

(defn- fib-sum [^long n]
  (let [r (loop [i 1
                 sum 0]
            (if (< i n)
              (recur (inc i) (+ sum (long (.fib fibonacci i))))
              sum))]
    r))

(defn -main [& args]
  (let [run-ms (parse-long (first args))
        n (parse-long (second args))
        _ (benchmark/run #(fib-sum n) run-ms)]
    (-> (benchmark/run #(fib-sum n) run-ms)
        benchmark/format-results 
        println)))

(comment
  (-main "10000" "36")
  :rcf)

