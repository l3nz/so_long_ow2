(ns so-long-ow2.translate
  (:gen-class))

(def FUNNYLEFT "↫")
(def FUNNYRIGHT "↬")

(defn hello
  "Plain example"
  []
  (+ 1 2))


;; http://ow2wiki.cvs.sourceforge.net/viewvc/ow2wiki/ow2wiki/DEVELOPERS/unitTest/owmlManager_unitTest.php?revision=1.3&view=markup


(defn prespace-code-blk
  "Trasforma il contenuto di un blocco CODE in
  una serie di righe spaziate"
  [s]
  (let [lines (clojure.string/split-lines s)]
    (apply str (map #( str "    " %1 "\n") lines))))


(defn node-from-name
  "Mancano i namespaces e le URL"

  [name]
  (if (.startsWith name "http:")
    name


  (let [lcname (.trim (.toLowerCase name))
        nospaces (clojure.string/replace lcname #"\s+" "_")]
    (str "node_" nospaces ".md"))

    ))


(defn replace-code
  "Replaces a block [#CODE:]...[:CODE#]"
  [src]
  (clojure.string/replace src
                          #"(?ims)\[#CODE:](.*?)\[:CODE#]"
                          #(prespace-code-blk (nth %1 1)))
  )


(defn replace-plugins
  "Replaces any plugins"
  [src]

 (clojure.string/replace src
                         #"(?msi)\[#(.+?)#]"
                         #(str "NO-PLUGIN:(" (nth %1 1) ")"  ))
  )



(defn create-md-link [link displayname]
  (let [fname (node-from-name link)]
    (str FUNNYLEFT displayname FUNNYRIGHT "(" fname ")")))


(defn replace-double-links
  "Replaces links like [a|b]"
  [src]

 (clojure.string/replace src
                         #"(?i)\[(.*?)\|(.+?)]"
                         #(create-md-link (nth %1 1) (nth %1 2)))
  )


(defn replace-single-links
  "Replaces links like [a]"
  [src]

 (clojure.string/replace src
                         #"(?i)\[(.*?)]"
                         #(create-md-link (nth %1 1) (nth %1 1)))
  )



(defn replace-monochars
  "Replaces [x] to <x>"
  [src]

 (clojure.string/replace src
                         #"(?msi)\[.]"
                         #(str "<" (nth %1 1) ">" ))
  )

(defn replace-bold
  "Il BOLD sono due virgolette"
  [src]
(clojure.string/replace src
                         #"(?i)\"\"(.+?)\"\""
                         #(str "*" (nth %1 1) "*" ))
)

(defn replace-italics
  "l'Italic sono due apicetti"
  [src]
(clojure.string/replace src
                         #"(?i)''(.+?)''"
                         #(str "**" (nth %1 1) "**" ))
)




(defn replace-funny-arrows [src]
  (let [leftrepl (clojure.string/replace src FUNNYLEFT "[")
        rightrepl (clojure.string/replace leftrepl FUNNYRIGHT "]") ]
    (str rightrepl)))




(defn translate-ow2
  "Translates some owml into markdown.
   See parser:
   http://ow2wiki.cvs.sourceforge.net/viewvc/ow2wiki/ow2wiki/includes/owmlManager.php?revision=1.13&view=markup
  "
  [owml]
  (-> owml
      replace-code
      replace-plugins
      replace-monochars
      replace-double-links
      replace-single-links
      replace-bold
      replace-italics
      replace-funny-arrows

      )


  )

