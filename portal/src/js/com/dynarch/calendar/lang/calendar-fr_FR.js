// ** I18N

// Calendar FR language
// Author: Mihai Bazon, <mihai_bazon@yahoo.com>
// Encoding: UTF-8
// Distributed under the same terms as the calendar itself.

// For translators: please use UTF-8 if possible.  We strongly believe that
// Unicode is the answer to a real internationalized world.  Also please
// include your contact information in the header, as can be seen above.

// Translator: David Duret, <pilgrim@mala-template.net> from previous french version
// Converted to UTF8 by Dan Lipofsky

// full day names
Calendar._DN = new Array
("Dimanche",
 "Lundi",
 "Mardi",
 "Mercredi",
 "Jeudi",
 "Vendredi",
 "Samedi",
 "Dimanche");

// First day of the week. "0" means display Sunday first, "1" means display
// Monday first, etc.
Calendar._FD = 0;

// Please note that the following array of short day names (and the same goes
// for short month names, _SMN) isn't absolutely necessary.  We give it here
// for exemplification on how one can customize the short day names, but if
// they are simply the first N letters of the full name you can simply say:
//
//   Calendar._SDN_len = N; // short day name length
//   Calendar._SMN_len = N; // short month name length
//
// If N = 3 then this is not needed either since we assume a value of 3 if not
// present, to be compatible with translation files that were written before
// this feature.

// short day names
Calendar._SDN = new Array
("Dim",
 "Lun",
 "Mar",
 "Mer",
 "Jeu",
 "Ven",
 "Sam",
 "Dim");

// full month names
Calendar._MN = new Array
("Janvier",
 "Février",
 "Mars",
 "Avril",
 "Mai",
 "Juin",
 "Juillet",
 "Août",
 "Septembre",
 "Octobre",
 "Novembre",
 "Décembre");

// short month names
Calendar._SMN = new Array
("Jan",
 "Fév",
 "Mar",
 "Avr",
 "Mai",
 "Juin",
 "Juil",
 "Août",
 "Sep",
 "Oct",
 "Nov",
 "Déc");

// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "À propos du calendrier";

Calendar._TT["ABOUT"] =
"DHTML Date/Heure Sélecteur\n" +
"(c) dynarch.com 2002-2003\n" + // don't translate this this ;-)
"Pour la dernière version visitez : http://dynarch.com/mishoo/calendar.epl\n" +
"Distribué sous licence GNU LGPL.  Voir http://gnu.org/licenses/lgpl.html pour les details." +
"\n\n" +
"Selection de la date :\n" +
"- Utiliser les bouttons \xab, \xbb  pour sélectionner l\'année\n" +
"- Utiliser les bouttons " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + " pour sélectionner les mois\n" +
"- Garder la souris sur n'importe quel bouton pour une sélection plus rapide";
Calendar._TT["ABOUT_TIME"] = "\n\n" +
"Sélection de l\'heure :\n" +
"- Cliquer sur heures ou minutes pour incrémenter\n" +
"- ou Maj-clic pour décrémenter\n" +
"- ou clic et glisser-déplacer pour une sélection plus rapide";

Calendar._TT["PREV_YEAR"] = "Année préc. (maintenir pour menu)";
Calendar._TT["PREV_MONTH"] = "Mois préc. (maintenir pour menu)";
Calendar._TT["GO_TODAY"] = "Atteindre la date du jour";
Calendar._TT["NEXT_MONTH"] = "Mois suiv. (maintenir pour menu)";
Calendar._TT["NEXT_YEAR"] = "Année suiv. (maintenir pour menu)";
Calendar._TT["SEL_DATE"] = "Sélectionner une date";
Calendar._TT["DRAG_TO_MOVE"] = "Déplacer";
Calendar._TT["PART_TODAY"] = " (Aujourd'hui)";

// the following is to inform that "%s" is to be the first day of week
// %s will be replaced with the day name.
Calendar._TT["DAY_FIRST"] = "Afficher %s en premier";

// This may be locale-dependent.  It specifies the week-end days, as an array
// of comma-separated numbers.  The numbers are from 0 to 6: 0 means Sunday, 1
// means Monday, etc.
Calendar._TT["WEEKEND"] = "0,6";

Calendar._TT["CLOSE"] = "Fermer";
Calendar._TT["TODAY"] = "Aujourd'hui";
Calendar._TT["TIME_PART"] = "(Maj-)Clic ou glisser pour modifier la valeur";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%d/%m/%Y";
Calendar._TT["TT_DATE_FORMAT"] = "%a, %b %e";

Calendar._TT["WK"] = "Sem.";
Calendar._TT["TIME"] = "Heure :";
