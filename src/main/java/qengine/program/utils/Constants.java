package qengine.program.utils;

public class Constants {
    public static final String ERROR_FILE_EXTENSION = "Les fichiers requête doivent avoir l'extension '.queryset' et le fichier de données '.nt'";
    public static final String ERROR_NO_FILE_NO_DIRECTORY = "Vous devez entrer un nom de fichier ou un dossier contenant des fichiers";
    public static final String ERROR_NO_ARGUMENTS = "Vous devez utiliser au minimum les arguments -query et -data";
    public static final String ERROR_IO = "Fichier dossier incorrect";
    public static final String ERROR_CSV_WRITER = "Une erreur s'est produite lors de l'écriture du csv";
    public static final String CSV_CREATED = "Votre fichier CSV a bien été créé";

    private Constants() {
    }
}
