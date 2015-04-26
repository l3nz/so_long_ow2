(ns so-long-ow2.translate-test
  (:require [clojure.test :refer :all]
            [so-long-ow2.translate :refer :all :as t]))

;(deftest a-test
;  (testing "FIXME, I fail."
;    (is (= 3 (hello)))))

(deftest codeblk
  (testing "Block CODE"
    (let [src "hello\n[#CODE:]hithere\nyothere[:CODE#]\nx"
          dst "hello\n    hithere\n    yothere\n\nx"
          prc (t/translate-ow2 src)
          ]
      (is (= dst prc)))))

(deftest plugins
  (testing "No plugins"
    (let [src "hello\n[#hithere\nyothere#]\nx"
          dst "hello\nNO-PLUGIN:(hithere\nyothere)\nx"
          prc (t/translate-ow2 src)
          ]
      (is (= dst prc)))))

(deftest double-links
  (testing "Double links"
    (let [src "hello [lnk|txt]\nx"
          dst "hello [txt](node_lnk.md)\nx"
          prc (t/translate-ow2 src)
          ]
      (is (= dst prc)))))

(deftest monochars
  (testing "Monos"
    (let [src "hello [x] [y]\nx"
          dst "hello <x> <y>\nx"
          prc (t/translate-ow2 src)
          ]
      (is (= dst prc)))))

(deftest single-links
  (testing "Single links"
    (let [src "hello [Lnk]\nx"
          dst "hello [Lnk](node_lnk.md)\nx"
          prc (t/translate-ow2 src)
          ]
      (is (= dst prc)))))

; [*] [http://www.grandstream.com/|Grandstream web site]

(deftest two-links-are-broken
  (testing "2 links are broken"
    (let [src "[*] [http://www.grandstream.com/|Grandstream web site]"
          dst "<*> [Grandstream web site](http://www.grandstream.com/)"
          prc (t/translate-ow2 src)
          ]
      (is (= dst prc)))))
