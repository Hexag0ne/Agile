# Agile

Projet de calcul de voyageur de commerce en client lourd Java en méthode 'Agile'
--
+ Lien vers outil de conduite de projet : https://webaedi.insa-lyon.fr/project/embaha-hexagone/backlog
+ installation de ObjectAid : http://www.objectaid.com/installation
+ installation de Junit : http://liris.cnrs.fr/pierre-edouard.portier/teaching_2016_2017/pld_agile/201610_Install_Cofoja_in_Eclipse.pdf
+ installation de eclemma : http://www.eclemma.org/installation.html

--
Librairie interface graphique : Swing
--
Modélisation de la carte :
Hashmap des noeuds (= carrefours) qui donne les coordonnées des noeuds
Hashmap de <origine, destination> -> information tronçons
Multimap <origine> -> toute les destinations atteignable directement à partir du noeud 'origine'
