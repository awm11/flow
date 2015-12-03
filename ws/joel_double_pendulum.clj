;; gorilla-repl.fileformat = 1

;; **
;;; #--------------------------------------
;;; # Joel Flow - Double Pendulum
;;; #--------------------------------------
;;; # Chaotic pendulum, with biased grammar
;; **

;; **
;;; Uses a grammar biased towards generating Lagrangians for simple physical systems.
;; **

;; @@
(ns flow.runs.doublependulum
  (:require [gorilla-plot.core :as plot]
            [algebolic.expression.core :as expression]
            [algebolic.expression.genetics :as genetics]
            [algebolic.expression.tree :as tree]
            [algebolic.expression.score :as score]
            [algebolic.expression.render :as render]
            [algebolic.expression.mma :as mma]
            [darwin.evolution.core :as evolution]
            [darwin.evolution.metrics :as metrics]
            [darwin.evolution.transform :as transform]
            [darwin.algorithms.spea2 :as spea2]
            [flow.lagrangian :as lagrangian]
            [flow.util :as util]
            [runs.config :as config]
            [runs.util :as rutil]
            [semantic-csv.core :as csv]
            [clojure.walk :as walk]))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; Load the data exported from Mathematica.
;; **

;; @@
(def data_theta1 (mapv #(vector (first %))
               (csv/slurp-csv "data/mma_double.csv"
                              :header false
                              :cast-fns {0 #(Double/parseDouble %) 1 #(Double/parseDouble %)})))
(def t-step 0.1)


(def data_omega1 (mapv #(vector (second %))
               (csv/slurp-csv "data/mma_double.csv"
                              :header false
                              :cast-fns {0 #(Double/parseDouble %) 1 #(Double/parseDouble %)}))) 
                       

(def t-step 0.1)


;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;flow.runs.doublependulum/t-step</span>","value":"#'flow.runs.doublependulum/t-step"}
;; <=

;; **
;;; **Theta**
;; **

;; @@
(plot/list-plot (mapv first data_theta1))
;; @@
;; =>
;;; {"type":"vega","content":{"axes":[{"scale":"x","type":"x"},{"scale":"y","type":"y"}],"scales":[{"name":"x","type":"linear","range":"width","zero":false,"domain":{"data":"77493e98-ef41-475c-a714-83b969d8fc8a","field":"data.x"}},{"name":"y","type":"linear","range":"height","nice":true,"zero":false,"domain":{"data":"77493e98-ef41-475c-a714-83b969d8fc8a","field":"data.y"}}],"marks":[{"type":"symbol","from":{"data":"77493e98-ef41-475c-a714-83b969d8fc8a"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"fill":{"value":"steelblue"},"fillOpacity":{"value":1}},"update":{"shape":"circle","size":{"value":70},"stroke":{"value":"transparent"}},"hover":{"size":{"value":210},"stroke":{"value":"white"}}}}],"data":[{"name":"77493e98-ef41-475c-a714-83b969d8fc8a","values":[{"x":0,"y":1.0},{"x":1,"y":0.8989199993243598},{"x":2,"y":0.6012264322527066},{"x":3,"y":0.20728721591523996},{"x":4,"y":-0.12469131936494138},{"x":5,"y":-0.3203010307328177},{"x":6,"y":-0.31739157814328817},{"x":7,"y":-0.15150573303836484},{"x":8,"y":-0.06853590752776278},{"x":9,"y":-0.10208675189016087},{"x":10,"y":-0.19283938714614973},{"x":11,"y":-0.3041667425077898},{"x":12,"y":-0.42784503777028354},{"x":13,"y":-0.5637214990503191},{"x":14,"y":-0.6436598262165486},{"x":15,"y":-0.556461294499259},{"x":16,"y":-0.2826385812681396},{"x":17,"y":0.11807334789353377},{"x":18,"y":0.4857189638152173},{"x":19,"y":0.6913021306892096},{"x":20,"y":0.7055517780524226},{"x":21,"y":0.5722506308257261},{"x":22,"y":0.4000601259592323},{"x":23,"y":0.23736876923428954},{"x":24,"y":0.09480122223273243},{"x":25,"y":0.002793018560010379},{"x":26,"y":0.020435421146520638},{"x":27,"y":0.18915121210814076},{"x":28,"y":0.29559964634164904},{"x":29,"y":0.19155971053107798},{"x":30,"y":-0.0645389453160641},{"x":31,"y":-0.41199979729428904},{"x":32,"y":-0.763745857310267},{"x":33,"y":-0.9658147523027288},{"x":34,"y":-0.9654151842309668},{"x":35,"y":-0.7619189567478477},{"x":36,"y":-0.3814639881360723},{"x":37,"y":0.019681487608635063},{"x":38,"y":0.30436890958202917},{"x":39,"y":0.4084348587488139},{"x":40,"y":0.3033511287685211},{"x":41,"y":0.16377080098790794},{"x":42,"y":0.13211037924768504},{"x":43,"y":0.1669624228955047},{"x":44,"y":0.22685663153362629},{"x":45,"y":0.29920237325085636},{"x":46,"y":0.3977196592588333},{"x":47,"y":0.5249176269470148},{"x":48,"y":0.5665113600861779},{"x":49,"y":0.4151813933396576},{"x":50,"y":0.08338014259205495},{"x":51,"y":-0.33945776612654516},{"x":52,"y":-0.6729538983437577},{"x":53,"y":-0.8190943458030526},{"x":54,"y":-0.7667122609067734},{"x":55,"y":-0.56613996650745},{"x":56,"y":-0.3329280745275617},{"x":57,"y":-0.12105327215003012},{"x":58,"y":0.04288629562864375},{"x":59,"y":0.10675009790370402},{"x":60,"y":-0.0012246218471329813},{"x":61,"y":-0.19771048010865605},{"x":62,"y":-0.20836327111761377},{"x":63,"y":-0.0473430071408562},{"x":64,"y":0.21536863452407284},{"x":65,"y":0.5318501320376852},{"x":66,"y":0.8203389351107336},{"x":67,"y":0.945085561544485},{"x":68,"y":0.8656880388502126},{"x":69,"y":0.5839868233179135},{"x":70,"y":0.15939401082395943},{"x":71,"y":-0.22285894688591146},{"x":72,"y":-0.44779233210495223},{"x":73,"y":-0.4661579631173108},{"x":74,"y":-0.32575683393984084},{"x":75,"y":-0.22180368665005942},{"x":76,"y":-0.18818819244095603},{"x":77,"y":-0.18576678271842334},{"x":78,"y":-0.1968715179619533},{"x":79,"y":-0.23362802935287189},{"x":80,"y":-0.33158415336253816},{"x":81,"y":-0.4725833079380909},{"x":82,"y":-0.47279998117485894},{"x":83,"y":-0.26485131605566004},{"x":84,"y":0.10776493903368044},{"x":85,"y":0.5367584498139469},{"x":86,"y":0.8344521790576684},{"x":87,"y":0.9313115251190204},{"x":88,"y":0.8240013888669246},{"x":89,"y":0.5514799939720119},{"x":90,"y":0.24539114522050504},{"x":91,"y":-0.014205895564683798},{"x":92,"y":-0.18284774301861834},{"x":93,"y":-0.19271063152967646},{"x":94,"y":-0.006238381238316786},{"x":95,"y":0.12534591122174707},{"x":96,"y":0.07394787024428637},{"x":97,"y":-0.09041820089881154},{"x":98,"y":-0.31111406108502415},{"x":99,"y":-0.5577880624881791},{"x":100,"y":-0.7747457021632537}]}],"width":400,"height":247.2187957763672,"padding":{"bottom":20,"top":10,"right":10,"left":50}},"value":"#gorilla_repl.vega.VegaView{:content {:axes [{:scale \"x\", :type \"x\"} {:scale \"y\", :type \"y\"}], :scales [{:name \"x\", :type \"linear\", :range \"width\", :zero false, :domain {:data \"77493e98-ef41-475c-a714-83b969d8fc8a\", :field \"data.x\"}} {:name \"y\", :type \"linear\", :range \"height\", :nice true, :zero false, :domain {:data \"77493e98-ef41-475c-a714-83b969d8fc8a\", :field \"data.y\"}}], :marks [{:type \"symbol\", :from {:data \"77493e98-ef41-475c-a714-83b969d8fc8a\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :y {:scale \"y\", :field \"data.y\"}, :fill {:value \"steelblue\"}, :fillOpacity {:value 1}}, :update {:shape \"circle\", :size {:value 70}, :stroke {:value \"transparent\"}}, :hover {:size {:value 210}, :stroke {:value \"white\"}}}}], :data [{:name \"77493e98-ef41-475c-a714-83b969d8fc8a\", :values ({:x 0, :y 1.0} {:x 1, :y 0.8989199993243598} {:x 2, :y 0.6012264322527066} {:x 3, :y 0.20728721591523996} {:x 4, :y -0.12469131936494138} {:x 5, :y -0.3203010307328177} {:x 6, :y -0.31739157814328817} {:x 7, :y -0.15150573303836484} {:x 8, :y -0.06853590752776278} {:x 9, :y -0.10208675189016087} {:x 10, :y -0.19283938714614973} {:x 11, :y -0.3041667425077898} {:x 12, :y -0.42784503777028354} {:x 13, :y -0.5637214990503191} {:x 14, :y -0.6436598262165486} {:x 15, :y -0.556461294499259} {:x 16, :y -0.2826385812681396} {:x 17, :y 0.11807334789353377} {:x 18, :y 0.4857189638152173} {:x 19, :y 0.6913021306892096} {:x 20, :y 0.7055517780524226} {:x 21, :y 0.5722506308257261} {:x 22, :y 0.4000601259592323} {:x 23, :y 0.23736876923428954} {:x 24, :y 0.09480122223273243} {:x 25, :y 0.002793018560010379} {:x 26, :y 0.020435421146520638} {:x 27, :y 0.18915121210814076} {:x 28, :y 0.29559964634164904} {:x 29, :y 0.19155971053107798} {:x 30, :y -0.0645389453160641} {:x 31, :y -0.41199979729428904} {:x 32, :y -0.763745857310267} {:x 33, :y -0.9658147523027288} {:x 34, :y -0.9654151842309668} {:x 35, :y -0.7619189567478477} {:x 36, :y -0.3814639881360723} {:x 37, :y 0.019681487608635063} {:x 38, :y 0.30436890958202917} {:x 39, :y 0.4084348587488139} {:x 40, :y 0.3033511287685211} {:x 41, :y 0.16377080098790794} {:x 42, :y 0.13211037924768504} {:x 43, :y 0.1669624228955047} {:x 44, :y 0.22685663153362629} {:x 45, :y 0.29920237325085636} {:x 46, :y 0.3977196592588333} {:x 47, :y 0.5249176269470148} {:x 48, :y 0.5665113600861779} {:x 49, :y 0.4151813933396576} {:x 50, :y 0.08338014259205495} {:x 51, :y -0.33945776612654516} {:x 52, :y -0.6729538983437577} {:x 53, :y -0.8190943458030526} {:x 54, :y -0.7667122609067734} {:x 55, :y -0.56613996650745} {:x 56, :y -0.3329280745275617} {:x 57, :y -0.12105327215003012} {:x 58, :y 0.04288629562864375} {:x 59, :y 0.10675009790370402} {:x 60, :y -0.0012246218471329813} {:x 61, :y -0.19771048010865605} {:x 62, :y -0.20836327111761377} {:x 63, :y -0.0473430071408562} {:x 64, :y 0.21536863452407284} {:x 65, :y 0.5318501320376852} {:x 66, :y 0.8203389351107336} {:x 67, :y 0.945085561544485} {:x 68, :y 0.8656880388502126} {:x 69, :y 0.5839868233179135} {:x 70, :y 0.15939401082395943} {:x 71, :y -0.22285894688591146} {:x 72, :y -0.44779233210495223} {:x 73, :y -0.4661579631173108} {:x 74, :y -0.32575683393984084} {:x 75, :y -0.22180368665005942} {:x 76, :y -0.18818819244095603} {:x 77, :y -0.18576678271842334} {:x 78, :y -0.1968715179619533} {:x 79, :y -0.23362802935287189} {:x 80, :y -0.33158415336253816} {:x 81, :y -0.4725833079380909} {:x 82, :y -0.47279998117485894} {:x 83, :y -0.26485131605566004} {:x 84, :y 0.10776493903368044} {:x 85, :y 0.5367584498139469} {:x 86, :y 0.8344521790576684} {:x 87, :y 0.9313115251190204} {:x 88, :y 0.8240013888669246} {:x 89, :y 0.5514799939720119} {:x 90, :y 0.24539114522050504} {:x 91, :y -0.014205895564683798} {:x 92, :y -0.18284774301861834} {:x 93, :y -0.19271063152967646} {:x 94, :y -0.006238381238316786} {:x 95, :y 0.12534591122174707} {:x 96, :y 0.07394787024428637} {:x 97, :y -0.09041820089881154} {:x 98, :y -0.31111406108502415} {:x 99, :y -0.5577880624881791} {:x 100, :y -0.7747457021632537})}], :width 400, :height 247.2188, :padding {:bottom 20, :top 10, :right 10, :left 50}}}"}
;; <=

;; **
;;; **Omega**
;; **

;; @@
(plot/list-plot (mapv first data_omega1))
;; @@
;; =>
;;; {"type":"vega","content":{"axes":[{"scale":"x","type":"x"},{"scale":"y","type":"y"}],"scales":[{"name":"x","type":"linear","range":"width","zero":false,"domain":{"data":"b6f63cd5-39ac-44a7-89a7-7744239d9714","field":"data.x"}},{"name":"y","type":"linear","range":"height","nice":true,"zero":false,"domain":{"data":"b6f63cd5-39ac-44a7-89a7-7744239d9714","field":"data.y"}}],"marks":[{"type":"symbol","from":{"data":"b6f63cd5-39ac-44a7-89a7-7744239d9714"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"fill":{"value":"steelblue"},"fillOpacity":{"value":1}},"update":{"shape":"circle","size":{"value":70},"stroke":{"value":"transparent"}},"hover":{"size":{"value":210},"stroke":{"value":"white"}}}}],"data":[{"name":"b6f63cd5-39ac-44a7-89a7-7744239d9714","values":[{"x":0,"y":0.5},{"x":1,"y":0.5273773323698951},{"x":2,"y":0.6150146061532324},{"x":3,"y":0.6867637450857415},{"x":4,"y":0.6128755226651414},{"x":5,"y":0.3759753033776718},{"x":6,"y":-0.00891222272756455},{"x":7,"y":-0.4859843812105411},{"x":8,"y":-0.8596378945099428},{"x":9,"y":-1.0880175001724761},{"x":10,"y":-1.1855488923462065},{"x":11,"y":-1.1575073908741338},{"x":12,"y":-1.0063385347323577},{"x":13,"y":-0.7409760400090484},{"x":14,"y":-0.42663491254056624},{"x":15,"y":-0.16332995583459806},{"x":16,"y":0.00313006770448885},{"x":17,"y":0.07657583412271292},{"x":18,"y":0.1636055429524412},{"x":19,"y":0.3409737545282742},{"x":20,"y":0.5938523347108549},{"x":21,"y":0.8689500002921885},{"x":22,"y":1.0703696819982962},{"x":23,"y":1.1499902909294584},{"x":24,"y":1.09921817125142},{"x":25,"y":0.9148241706177372},{"x":26,"y":0.5887099607550005},{"x":27,"y":0.11546087486737448},{"x":28,"y":-0.3352012939175865},{"x":29,"y":-0.6374287103167247},{"x":30,"y":-0.7847775717140719},{"x":31,"y":-0.7671413329890023},{"x":32,"y":-0.637892645069462},{"x":33,"y":-0.5291651228069204},{"x":34,"y":-0.4769995584447674},{"x":35,"y":-0.48575035287369117},{"x":36,"y":-0.5532310885650046},{"x":37,"y":-0.5531692758934087},{"x":38,"y":-0.3912589310921021},{"x":39,"y":-0.0779292378679363},{"x":40,"y":0.3606477936759421},{"x":41,"y":0.7774445959489588},{"x":42,"y":1.052618500116024},{"x":43,"y":1.1955554771672163},{"x":44,"y":1.2153438737438662},{"x":45,"y":1.1141115554095216},{"x":46,"y":0.8889440285610497},{"x":47,"y":0.5513810440539018},{"x":48,"y":0.20244026369872878},{"x":49,"y":-0.05681057672314274},{"x":50,"y":-0.19160561660471556},{"x":51,"y":-0.2285229711276155},{"x":52,"y":-0.2989790415121534},{"x":53,"y":-0.45480098142639636},{"x":54,"y":-0.6749425063827657},{"x":55,"y":-0.9094739078608727},{"x":56,"y":-1.0575241567889448},{"x":57,"y":-1.0698642995934966},{"x":58,"y":-0.9397360032321105},{"x":59,"y":-0.6674124464434056},{"x":60,"y":-0.2399759747967363},{"x":61,"y":0.2667689694170905},{"x":62,"y":0.6400580579094078},{"x":63,"y":0.8623641661791187},{"x":64,"y":0.9296901822527296},{"x":65,"y":0.8414607796897818},{"x":66,"y":0.6619901861846117},{"x":67,"y":0.5084990646688763},{"x":68,"y":0.4130530475111119},{"x":69,"y":0.3918159994019021},{"x":70,"y":0.4201765296637072},{"x":71,"y":0.3507218638943464},{"x":72,"y":0.12668887943057722},{"x":73,"y":-0.2295676834221329},{"x":74,"y":-0.65183281816219},{"x":75,"y":-0.9792526718782775},{"x":76,"y":-1.1714530923300341},{"x":77,"y":-1.2398543020068622},{"x":78,"y":-1.188508569195829},{"x":79,"y":-1.0145663696594325},{"x":80,"y":-0.7077893015816162},{"x":81,"y":-0.29694687500599043},{"x":82,"y":0.06550229656748253},{"x":83,"y":0.301772028672801},{"x":84,"y":0.3872985570654786},{"x":85,"y":0.36943128095627253},{"x":86,"y":0.39598336347445034},{"x":87,"y":0.49961026012135046},{"x":88,"y":0.6624847467718888},{"x":89,"y":0.8519426659243755},{"x":90,"y":0.9543707079885553},{"x":91,"y":0.9045250714705957},{"x":92,"y":0.7006813353503054},{"x":93,"y":0.34712433586419034},{"x":94,"y":-0.1521973416376246},{"x":95,"y":-0.6015989351938787},{"x":96,"y":-0.8940901509067573},{"x":97,"y":-1.0427348850024962},{"x":98,"y":-1.0464947142202496},{"x":99,"y":-0.9108663835615147},{"x":100,"y":-0.6852351460783989}]}],"width":400,"height":247.2187957763672,"padding":{"bottom":20,"top":10,"right":10,"left":50}},"value":"#gorilla_repl.vega.VegaView{:content {:axes [{:scale \"x\", :type \"x\"} {:scale \"y\", :type \"y\"}], :scales [{:name \"x\", :type \"linear\", :range \"width\", :zero false, :domain {:data \"b6f63cd5-39ac-44a7-89a7-7744239d9714\", :field \"data.x\"}} {:name \"y\", :type \"linear\", :range \"height\", :nice true, :zero false, :domain {:data \"b6f63cd5-39ac-44a7-89a7-7744239d9714\", :field \"data.y\"}}], :marks [{:type \"symbol\", :from {:data \"b6f63cd5-39ac-44a7-89a7-7744239d9714\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :y {:scale \"y\", :field \"data.y\"}, :fill {:value \"steelblue\"}, :fillOpacity {:value 1}}, :update {:shape \"circle\", :size {:value 70}, :stroke {:value \"transparent\"}}, :hover {:size {:value 210}, :stroke {:value \"white\"}}}}], :data [{:name \"b6f63cd5-39ac-44a7-89a7-7744239d9714\", :values ({:x 0, :y 0.5} {:x 1, :y 0.5273773323698951} {:x 2, :y 0.6150146061532324} {:x 3, :y 0.6867637450857415} {:x 4, :y 0.6128755226651414} {:x 5, :y 0.3759753033776718} {:x 6, :y -0.00891222272756455} {:x 7, :y -0.4859843812105411} {:x 8, :y -0.8596378945099428} {:x 9, :y -1.0880175001724761} {:x 10, :y -1.1855488923462065} {:x 11, :y -1.1575073908741338} {:x 12, :y -1.0063385347323577} {:x 13, :y -0.7409760400090484} {:x 14, :y -0.42663491254056624} {:x 15, :y -0.16332995583459806} {:x 16, :y 0.00313006770448885} {:x 17, :y 0.07657583412271292} {:x 18, :y 0.1636055429524412} {:x 19, :y 0.3409737545282742} {:x 20, :y 0.5938523347108549} {:x 21, :y 0.8689500002921885} {:x 22, :y 1.0703696819982962} {:x 23, :y 1.1499902909294584} {:x 24, :y 1.09921817125142} {:x 25, :y 0.9148241706177372} {:x 26, :y 0.5887099607550005} {:x 27, :y 0.11546087486737448} {:x 28, :y -0.3352012939175865} {:x 29, :y -0.6374287103167247} {:x 30, :y -0.7847775717140719} {:x 31, :y -0.7671413329890023} {:x 32, :y -0.637892645069462} {:x 33, :y -0.5291651228069204} {:x 34, :y -0.4769995584447674} {:x 35, :y -0.48575035287369117} {:x 36, :y -0.5532310885650046} {:x 37, :y -0.5531692758934087} {:x 38, :y -0.3912589310921021} {:x 39, :y -0.0779292378679363} {:x 40, :y 0.3606477936759421} {:x 41, :y 0.7774445959489588} {:x 42, :y 1.052618500116024} {:x 43, :y 1.1955554771672163} {:x 44, :y 1.2153438737438662} {:x 45, :y 1.1141115554095216} {:x 46, :y 0.8889440285610497} {:x 47, :y 0.5513810440539018} {:x 48, :y 0.20244026369872878} {:x 49, :y -0.05681057672314274} {:x 50, :y -0.19160561660471556} {:x 51, :y -0.2285229711276155} {:x 52, :y -0.2989790415121534} {:x 53, :y -0.45480098142639636} {:x 54, :y -0.6749425063827657} {:x 55, :y -0.9094739078608727} {:x 56, :y -1.0575241567889448} {:x 57, :y -1.0698642995934966} {:x 58, :y -0.9397360032321105} {:x 59, :y -0.6674124464434056} {:x 60, :y -0.2399759747967363} {:x 61, :y 0.2667689694170905} {:x 62, :y 0.6400580579094078} {:x 63, :y 0.8623641661791187} {:x 64, :y 0.9296901822527296} {:x 65, :y 0.8414607796897818} {:x 66, :y 0.6619901861846117} {:x 67, :y 0.5084990646688763} {:x 68, :y 0.4130530475111119} {:x 69, :y 0.3918159994019021} {:x 70, :y 0.4201765296637072} {:x 71, :y 0.3507218638943464} {:x 72, :y 0.12668887943057722} {:x 73, :y -0.2295676834221329} {:x 74, :y -0.65183281816219} {:x 75, :y -0.9792526718782775} {:x 76, :y -1.1714530923300341} {:x 77, :y -1.2398543020068622} {:x 78, :y -1.188508569195829} {:x 79, :y -1.0145663696594325} {:x 80, :y -0.7077893015816162} {:x 81, :y -0.29694687500599043} {:x 82, :y 0.06550229656748253} {:x 83, :y 0.301772028672801} {:x 84, :y 0.3872985570654786} {:x 85, :y 0.36943128095627253} {:x 86, :y 0.39598336347445034} {:x 87, :y 0.49961026012135046} {:x 88, :y 0.6624847467718888} {:x 89, :y 0.8519426659243755} {:x 90, :y 0.9543707079885553} {:x 91, :y 0.9045250714705957} {:x 92, :y 0.7006813353503054} {:x 93, :y 0.34712433586419034} {:x 94, :y -0.1521973416376246} {:x 95, :y -0.6015989351938787} {:x 96, :y -0.8940901509067573} {:x 97, :y -1.0427348850024962} {:x 98, :y -1.0464947142202496} {:x 99, :y -0.9108663835615147} {:x 100, :y -0.6852351460783989})}], :width 400, :height 247.2188, :padding {:bottom 20, :top 10, :right 10, :left 50}}}"}
;; <=

;; **
;;; As well as the variables themselves, sines, cosines and squares of the variables are used as terminal nodes of the expression trees. The branch nodes are arithmetic operators.
;; **

;; @@
(def vars '[theta1 omega1 theta2 omega2 cos-omega1 sin-omega1 cos-theta1 sin-theta1 theta1-sq omega1-sq cos-omega2 sin-omega2 cos-theta2 sin-theta2 theta2-sq omega2-sq])

(count vars)

(def functions (expression/get-functions #{:plus :minus :times}))

(def terminals (expression/terminal-generators vars))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;flow.runs.doublependulum/terminals</span>","value":"#'flow.runs.doublependulum/terminals"}
;; <=

;; **
;;; This function transforms expressions in our biased grammar into canonical algebolic expressions.
;; **

;; @@
(defn grammar-replace
  [expr]
  (walk/postwalk-replace
    {'sin-theta1 '[:sin theta1]
     'cos-theta1 '[:cos theta1]
     'sin-omega1 '[:sin omega1]
     'cos-omega1 '[:cos omega1]
     'theta1-sq '[:times theta1 theta1]
     'omega1-sq '[:times omega1 omega1]
     'theta1-cube '[:times [:times theta1 theta1] theta1]
     'omega1-cube '[:times [:times omega1 omega1] omega1]
     'theta1-four '[:times [:times theta1 theta1] [:times theta1 theta1]]
     'omega1-four '[:times [:times omega1 omega1] [:times omega1 omega1]]
     'sin-theta2 '[:sin theta2]
     'cos-theta2 '[:cos theta2]
     'sin-omega2 '[:sin omega2]
     'cos-omega2 '[:cos omega2]
     'theta2-sq '[:times theta2 theta2]
     'omega2-sq '[:times omega2 omega2]
     'theta2-cube '[:times [:times theta2 theta2] theta2]
     'omega2-cube '[:times [:times omega2 omega2] omega2]
     'theta2-four '[:times [:times theta2 theta2] [:times theta2 theta2]]
     'omega2-four '[:times [:times omega2 omega2] [:times omega2 omega2]]
     } expr))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;flow.runs.doublependulum/grammar-replace</span>","value":"#'flow.runs.doublependulum/grammar-replace"}
;; <=

;; **
;;; Configure the evolutionary algorithm. See the darwin code for details.
;; **

;; @@
(def generation-config
  (let [size-limit 50
        ;;min-size 1
        ea-config (spea2/spea2-config
                    {:goals [:error :complexity]
                     :archive-size 50
                     :comparison-depth 3
                     :deduplicate true
                     :binary-ops [{:op (partial genetics/ninety-ten-sl-crossover size-limit) :repeat 45}]
                     :unary-ops [{:op (partial genetics/random-tree-mutation functions terminals 5) :repeat 10}]})
        score-functions {:complexity (fn [x] (+ (* 0.0 (tree/depth x)) (* 1.0 (tree/count-nodes x))))
                         :error      (fn [e] (lagrangian/lagrange-score-mmd
                                               (lagrangian/prepare-lagrange-mmd-data data_theta1 t-step '[theta1 theta2])
                                               t-step '[theta1 theta2] '[omega1 omega2] (grammar-replace e)))}]
    {:ea-config          ea-config
     :transformations    [(partial transform/apply-to-fraction-of-genotypes
                                   (partial transform/hill-descent
                                            genetics/twiddle-constants
                                            (:error score-functions))
                                   0.20)
                          (partial transform/apply-to-all-genotypes
                                   (partial genetics/trim-hard size-limit))
                          #_(partial transform/apply-to-all-genotypes
                                     (partial genetics/boost-hard min-size
                                              #(genetics/random-full-tree functions terminals 3)))]
     :score-functions    score-functions
     :reporting-function (fn [z] (print ".") (flush))}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;flow.runs.doublependulum/generation-config</span>","value":"#'flow.runs.doublependulum/generation-config"}
;; <=

;; **
;;; Create an initial population and evolve for a number of generations.
;; **

;; @@
(def initial-zeitgeist (evolution/make-zeitgeist (genetics/full-population functions terminals 100 7)))

; A quick look at the first population 

(mma/fullform (grammar-replace (:genotype (first (initial-zeitgeist :rabble)))))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Plus[Subtract[Sin[omega2], Cos[omega2]], Plus[Sin[theta1], Times[omega1, omega1]]]&quot;</span>","value":"\"Plus[Subtract[Sin[omega2], Cos[omega2]], Plus[Sin[theta1], Times[omega1, omega1]]]\""}
;; <=

;; @@
(time (def result (evolution/run-evolution
                    generation-config
                    initial-zeitgeist
                    (fn [zg gc] (>= (:age zg) 100)))))
;; @@

;; **
;;; Look at the best error as a function of generation number.
;; **

;; @@
(plot/list-plot (:min (:error @metrics/metrics)) :joined true)
;; @@
;; =>
;;; {"type":"vega","content":{"axes":[{"scale":"x","type":"x"},{"scale":"y","type":"y"}],"scales":[{"name":"x","type":"linear","range":"width","zero":false,"domain":{"data":"e497a01b-9b64-4b1a-90ba-8ec26f8895ea","field":"data.x"}},{"name":"y","type":"linear","range":"height","nice":true,"zero":false,"domain":{"data":"e497a01b-9b64-4b1a-90ba-8ec26f8895ea","field":"data.y"}}],"marks":[{"type":"line","from":{"data":"e497a01b-9b64-4b1a-90ba-8ec26f8895ea"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"stroke":{"value":"#FF29D2"},"strokeWidth":{"value":2},"strokeOpacity":{"value":1}}}}],"data":[{"name":"e497a01b-9b64-4b1a-90ba-8ec26f8895ea","values":[]}],"width":400,"height":247.2187957763672,"padding":{"bottom":20,"top":10,"right":10,"left":50}},"value":"#gorilla_repl.vega.VegaView{:content {:axes [{:scale \"x\", :type \"x\"} {:scale \"y\", :type \"y\"}], :scales [{:name \"x\", :type \"linear\", :range \"width\", :zero false, :domain {:data \"e497a01b-9b64-4b1a-90ba-8ec26f8895ea\", :field \"data.x\"}} {:name \"y\", :type \"linear\", :range \"height\", :nice true, :zero false, :domain {:data \"e497a01b-9b64-4b1a-90ba-8ec26f8895ea\", :field \"data.y\"}}], :marks [{:type \"line\", :from {:data \"e497a01b-9b64-4b1a-90ba-8ec26f8895ea\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :y {:scale \"y\", :field \"data.y\"}, :stroke {:value \"#FF29D2\"}, :strokeWidth {:value 2}, :strokeOpacity {:value 1}}}}], :data [{:name \"e497a01b-9b64-4b1a-90ba-8ec26f8895ea\", :values ()}], :width 400, :height 247.2188, :padding {:bottom 20, :top 10, :right 10, :left 50}}}"}
;; <=

;; **
;;; Examine the pareto front.
;; **

;; @@
(rutil/pareto-plot-population [:complexity :error] result)
;; @@
;; =>
;;; {"type":"vega","content":{"width":400,"height":247.2187957763672,"padding":{"bottom":20,"top":10,"right":10,"left":50},"scales":[{"name":"x","type":"linear","range":"width","zero":false,"domain":{"data":"ea9cda2f-792b-46e8-821e-f829bb606e88","field":"data.x"}},{"name":"y","type":"linear","range":"height","nice":true,"zero":false,"domain":{"data":"ea9cda2f-792b-46e8-821e-f829bb606e88","field":"data.y"}}],"axes":[{"scale":"x","type":"x"},{"scale":"y","type":"y"}],"data":[{"name":"ea9cda2f-792b-46e8-821e-f829bb606e88","values":[]},{"name":"23912cd9-b328-486b-b671-c5c213c1f719","values":[]},{"name":"68f0d021-5ffc-4282-9eb0-6637829f8d17","values":[]}],"marks":[{"type":"symbol","from":{"data":"ea9cda2f-792b-46e8-821e-f829bb606e88"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"fill":{"value":"red"},"fillOpacity":{"value":1}},"update":{"shape":"circle","size":{"value":70},"stroke":{"value":"transparent"}},"hover":{"size":{"value":210},"stroke":{"value":"white"}}}},{"type":"symbol","from":{"data":"23912cd9-b328-486b-b671-c5c213c1f719"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"fill":{"value":"blue"},"fillOpacity":{"value":1}},"update":{"shape":"circle","size":{"value":70},"stroke":{"value":"transparent"}},"hover":{"size":{"value":210},"stroke":{"value":"white"}}}},{"type":"symbol","from":{"data":"68f0d021-5ffc-4282-9eb0-6637829f8d17"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"fill":{"value":"#ff29d2"},"fillOpacity":{"value":1}},"update":{"shape":"circle","size":{"value":70},"stroke":{"value":"transparent"}},"hover":{"size":{"value":210},"stroke":{"value":"white"}}}}]},"value":"#gorilla_repl.vega.VegaView{:content {:width 400, :height 247.2188, :padding {:bottom 20, :top 10, :right 10, :left 50}, :scales [{:name \"x\", :type \"linear\", :range \"width\", :zero false, :domain {:data \"ea9cda2f-792b-46e8-821e-f829bb606e88\", :field \"data.x\"}} {:name \"y\", :type \"linear\", :range \"height\", :nice true, :zero false, :domain {:data \"ea9cda2f-792b-46e8-821e-f829bb606e88\", :field \"data.y\"}}], :axes [{:scale \"x\", :type \"x\"} {:scale \"y\", :type \"y\"}], :data ({:name \"ea9cda2f-792b-46e8-821e-f829bb606e88\", :values ()} {:name \"23912cd9-b328-486b-b671-c5c213c1f719\", :values ()} {:name \"68f0d021-5ffc-4282-9eb0-6637829f8d17\", :values ()}), :marks ({:type \"symbol\", :from {:data \"ea9cda2f-792b-46e8-821e-f829bb606e88\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :y {:scale \"y\", :field \"data.y\"}, :fill {:value \"red\"}, :fillOpacity {:value 1}}, :update {:shape \"circle\", :size {:value 70}, :stroke {:value \"transparent\"}}, :hover {:size {:value 210}, :stroke {:value \"white\"}}}} {:type \"symbol\", :from {:data \"23912cd9-b328-486b-b671-c5c213c1f719\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :y {:scale \"y\", :field \"data.y\"}, :fill {:value \"blue\"}, :fillOpacity {:value 1}}, :update {:shape \"circle\", :size {:value 70}, :stroke {:value \"transparent\"}}, :hover {:size {:value 210}, :stroke {:value \"white\"}}}} {:type \"symbol\", :from {:data \"68f0d021-5ffc-4282-9eb0-6637829f8d17\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :y {:scale \"y\", :field \"data.y\"}, :fill {:value \"#ff29d2\"}, :fillOpacity {:value 1}}, :update {:shape \"circle\", :size {:value 70}, :stroke {:value \"transparent\"}}, :hover {:size {:value 210}, :stroke {:value \"white\"}}}})}}"}
;; <=

;; **
;;; Inspect an element of the pareto front. We are interested in the simplest expression with a low error.
;; **

;; @@
(def n 28)
(def r (nth (map :genotype (sort-by :complexity (:elite result))) n))
(tree/count-nodes r)
(def rr (grammar-replace r))
(tree/count-nodes rr)

(lagrangian/lagrange-score-mmd
  (lagrangian/prepare-lagrange-mmd-data dat t-step '[theta])
  t-step '[theta] '[omega] rr)

(render/mathematician-view rr)

(mma/fullform rr)

rr
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>#&lt;Unbound Unbound: #&#x27;flow.runs.doublependulum/r&gt;</span>","value":"#<Unbound Unbound: #'flow.runs.doublependulum/r>"}
;; <=

;; @@

;; @@
