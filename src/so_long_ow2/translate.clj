(ns so-long-ow2.translate
  (:require [clojure.string :as ss])
  (:gen-class))

(def FUNNYLEFT "↫")
(def FUNNYRIGHT "↬")


;; http://ow2wiki.cvs.sourceforge.net/viewvc/ow2wiki/ow2wiki/DEVELOPERS/unitTest/owmlManager_unitTest.php?revision=1.3&view=markup

(defn funnyencode
  "Quota le parentesi quadre"
  [src]
  (let [leftrepl (ss/replace src  "[" FUNNYLEFT)
        rightrepl (ss/replace leftrepl  "]" FUNNYRIGHT) ]
    (str rightrepl)))



(defn replace-funny-arrows
  "Rimette le parentesi quadre"
  [src]
  (let [leftrepl (ss/replace src FUNNYLEFT "[")
        rightrepl (ss/replace leftrepl FUNNYRIGHT "]") ]
    (str rightrepl)))





(defn prespace-code-blk
  "Trasforma il contenuto di un blocco CODE in
  una serie di righe spaziate"
  [s]
  (let [lines (ss/split-lines s)]
    (apply str (map #( str "    " (funnyencode %1) "\n") lines))))

(defn mkString [s]
  (if (nil? s)
    ""
    (-> s
        ss/trim
        ss/lower-case)))


(defn samePage? [p1 p2]
  (= (mkString p1) (mkString p2)))

;; {% post_url 2010-07-21-name-of-post %}
(defn fetchPageUrl
  [pname nodehash]
  (let [elem (first (filterv #(samePage? pname (:title %)) nodehash))
        file (:fname elem)
        ]
    (if (nil? file)
      (do
        (println "WTF: " pname)
        (str "-NOTFOUND-" pname))
      (str "{% post_url " file " %}")
      )))



(defn node-from-name
  [name nodehash]
  (cond
    (.startsWith name "http:") name
    (.startsWith name "https:") name
    :else (fetchPageUrl name nodehash)))



(defn replace-code
  "Replaces a block [#CODE:]...[:CODE#]"
  [src]
  (ss/replace src
                          #"(?ims)\[#CODE:](.*?)\[:CODE#]"
                          #(prespace-code-blk (nth %1 1)))
  )


(defn replace-plugins
  "Replaces any plugins"
  [src]

 (ss/replace src
                         #"(?msi)\[#(.+?)#]"
                         #(str "NO-PLUGIN:(" (nth %1 1) ")"  ))
  )


;; [Name of Link]({% post_url 2010-07-21-name-of-post %})

(defn create-md-link [link displayname nodehash]
  (let [fname (node-from-name link nodehash)]
    (str FUNNYLEFT displayname FUNNYRIGHT "(" fname ")")))


(defn replace-double-links
  "Replaces links like [a|b]"
  [src nodehash]

 (ss/replace src
                         #"(?i)\[(.*?)\|(.+?)]"
                         #(create-md-link (nth %1 1) (nth %1 2) nodehash))
  )


(defn replace-single-links
  "Replaces links like [a]"
  [src nodehash]

 (ss/replace src
                         #"(?i)\[(.*?)]"
                         #(create-md-link (nth %1 1) (nth %1 1) nodehash))
  )


(defn replace-monochar
  "Rimpiazza un carattere monochar"
  [c]
  (case c
    "[*]" "\n*"
    (str "<" (nth c 1) ">" )))

(defn replace-monochars
  "Replaces [x] to <x>"
  [src]

 (ss/replace src
                         #"(?msi)\[.]"
                         replace-monochar
  ))
; #(str "<" (nth %1 1) ">" )

(defn replace-bold
  "Il BOLD sono due virgolette"
  [src]
(ss/replace src
                         #"(?i)\"\"(.+?)\"\""
                         #(str "**" (nth %1 1) "**\n" ))
)

(defn replace-italics
  "l'Italic sono due apicetti"
  [src]
(ss/replace src
                         #"(?i)''(.+?)''"
                         #(str "*" (nth %1 1) "*" ))
)




(defn translate-ow2
  "Translates some owml into markdown.
   See parser:
   http://ow2wiki.cvs.sourceforge.net/viewvc/ow2wiki/ow2wiki/includes/owmlManager.php?revision=1.13&view=markup
  "
  [owml nodehash]
  (-> owml
      replace-code
      replace-plugins
      replace-monochars
      (replace-double-links nodehash)
      (replace-single-links nodehash)
      replace-bold
      replace-italics
      replace-funny-arrows

      )
  )


