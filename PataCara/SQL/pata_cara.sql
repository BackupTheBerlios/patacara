# phpMyAdmin MySQL-Dump
# version 2.2.6
# http://phpwizard.net/phpMyAdmin/
# http://www.phpmyadmin.net/ (download page)
#
# Serveur: localhost
# Généré le : Samedi 29 Mars 2003 à 01:28
# Version du serveur: 3.23.49
# Version de PHP: 4.2.0
# Base de données: `pata_cara`
# --------------------------------------------------------

#
# Structure de la table `membre_pata`
#

DROP TABLE IF EXISTS `membre_pata`;
CREATE TABLE `membre_pata` (
  `id_membre` int(11) NOT NULL auto_increment,
  `pseudo` varchar(30) NOT NULL default '',
  `password` varchar(30) NOT NULL default '',
  `nom` varchar(30) default NULL,
  `prenom` varchar(30) default NULL,
  `age` smallint(3) default NULL,
  `ville` varchar(30) default NULL,
  `groupe` varchar(30) default NULL,
  `loisir` tinytext,
  `sexe` char(1) default NULL,
  `nbLoisir` tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (`id_membre`),
  UNIQUE KEY `pseudo` (`pseudo`)
) TYPE=MyISAM COMMENT='contient la liste de tous les membres de pata_cara';

#
# Contenu de la table `membre_pata`
#

INSERT INTO `membre_pata` (`id_membre`, `pseudo`, `password`, `nom`, `prenom`, `age`, `ville`, `groupe`, `loisir`, `sexe`, `nbLoisir`) VALUES (1, 'pat', 'p', 'giraud', 'remy', 21, 'nice', 'étudiant', 'volley tennis', 'H', 2);
INSERT INTO `membre_pata` (`id_membre`, `pseudo`, `password`, `nom`, `prenom`, `age`, `ville`, `groupe`, `loisir`, `sexe`, `nbLoisir`) VALUES (2, 'gpoulet', 'g', 'poulet', 'géraldine', 21, 'belgique', 'étudiant', 'hand', 'F', 1);
INSERT INTO `membre_pata` (`id_membre`, `pseudo`, `password`, `nom`, `prenom`, `age`, `ville`, `groupe`, `loisir`, `sexe`, `nbLoisir`) VALUES (6, 'pierroooot', 'azerty', 'Pierrit', 'Pierrot', 20, 'gnagnagna', 'étudiant', 'natation', 'H', 1);
INSERT INTO `membre_pata` (`id_membre`, `pseudo`, `password`, `nom`, `prenom`, `age`, `ville`, `groupe`, `loisir`, `sexe`, `nbLoisir`) VALUES (4, 'culass', 'naz', 'Nazarian', 'Luc', 20, 'aix', 'retraité', 'volley tennis cheval course natation', 'H', 5);
INSERT INTO `membre_pata` (`id_membre`, `pseudo`, `password`, `nom`, `prenom`, `age`, `ville`, `groupe`, `loisir`, `sexe`, `nbLoisir`) VALUES (5, 'lilie', 'choups', 'Ansart', 'Aurélie', 19, 'Aix', 'étudiant', 'tennis', 'F', 1);

