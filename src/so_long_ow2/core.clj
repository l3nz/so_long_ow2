(ns so-long-ow2.core
  (:require
    [clojure.java.jdbc :as sql]
    [so-long-ow2.translate :as translate]
  )
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))


;;
;; Per farlo andare, vado su instarepl e chiamo
;;  (writeAll)
;;


(def db {:classname "com.mysql.jdbc.Driver"
         :subprotocol "mysql"
         :subname "//localhost:3306/ow2_astrecipes"
         :user ""
         :password ""})

(def OUTDIR "/Users/lenz/dev/git_loway/astrecipes_v2/astrecipes/_posts/")


(defn qq
  " Una query SQL "
  [q]
  (sql/query db
        [q] ))


(defn fileName [dt title]
  (let [dtTxt (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") dt)
        ts ( -> title
                clojure.string/lower-case
                (clojure.string/replace #"\s+" " ")
                (clojure.string/replace #" " "-")
             )]
  ( str dtTxt "-" ts )))



(defn loadNodeText [nodeId]
  (first (qq (str
    "SELECT N.title, N.dtCreation, T.text, N.idNode
       FROM ow2_nodes N, ow2_texts T
      WHERE N.`idNode` = T.`idNode`
        AND T.`isValid` = 1
        AND N.`idArea`  = 4
        AND T.textType = 1
        AND N.idNode = " nodeId))))

(defn loadNodeTags [nodeId]
  (let [q ( str "SELECT T.tag
FROM ow2_nodes_tags NT, ow2_tags T
WHERE NT.`tag_id` = T.`id_tag`
  AND NT.`node_id` = " nodeId)]
     (map :tag (qq q))))


;; il nodo 101 e' corto
(defn loadNode [nodeId]
  (let [node (loadNodeText nodeId)
        tags (loadNodeTags nodeId)
        file (fileName (:dtcreation node) (:title node))]
    (assoc node :tags tags :fname file)))


(defn mkPreamble [node]
  (let [tags (apply str (map #(str " - " % "\n") (:tags node)))]
  (str "---
layout: post
title:  " (:title node) "
date:   " (:dtcreation node) "
tags:\n" tags "
categories: update
---\n\n")))


(defn nodeToFile
  "Legge un nodo e scrive un file"
  [nodeId nodehash]
  (let [node (loadNode nodeId)
        preamble (mkPreamble node)
        body (translate/translate-ow2 (:text node) nodehash)
        fname (:fname node)
        debug ( str "## NODEID: " (:idnode node) "\n\n")
        all (str preamble body)]
    (spit (str OUTDIR fname ".md") all)))


(defn findAllNodes
  "Trova tutti i nodi validi e restituisce gli ID"
  []
  (vec (map :idnode
        (qq "SELECT  N.idNode
      FROM ow2_nodes N, ow2_texts T
      WHERE N.`idNode` = T.`idNode`
        AND T.`isValid` = 1
        AND N.`idArea`  = 4
        AND T.textType = 1"))))


(defn mkNodeHash
  "Carica un hash di tutti i nodi"
  [vNodes]
  (vec (map loadNode vNodes)))

(defn mkDebugNodeHash []
  (mkNodeHash (findAllNodes)))


(defn writeAll []
  (let [nodes (findAllNodes)
        nodehash (mkNodeHash nodes)
        ]
    (map #(nodeToFile % nodehash) nodes)))
