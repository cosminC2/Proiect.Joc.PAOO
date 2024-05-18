package PaooGame.Tiles;

import java.awt.*;
import java.awt.image.BufferedImage;

/*! \class public class Tile
    \brief Retine toate dalele intr-un vector si ofera posibilitatea regasirii dupa un id.
 */
public class Tile
{
    private static final int NO_TILES   = 32;
    public static Tile[] tiles          = new Tile[NO_TILES*NO_TILES];       /*!< Vector de referinte de tipuri de dale.*/
    public static Tile[] tileList     = new Tile[NO_TILES*NO_TILES];
    static {
            for(int x = 0;x<NO_TILES*NO_TILES;x++)
                tileList[x] = new UniversalTile(x);

    }
    //a fost optimizat modul de randare a tile-urilor intr-un singur array de 32^2
    //astfel, pentru fiecare tile trebuie sa fac niste calcule
    //dar ma lasa introducerea eficienta a 32^2 tile-uri
    //si nu trebuie sa fac efectiv 1024 clase
    //ignorand partea in care nu au fost folosite decat vreo 60 ish de tileuri
    //procesul de mappare a hartii nivelului a durat 4 ore, si maine trebuie predat proiectul
    //asadar nivelele 2/3 vor folosi aceeasi harta, doar ca puse in fisiere diferite numite corespunzator




    //incercarea mea batuta in cap de a reface sistemul de tile-uri care e si mai batut in cap


        /// De remarcat ca urmatoarele dale sunt statice si publice. Acest lucru imi permite sa le am incarcate
        /// o singura data in memorie
    public static Tile cursorTile       = new CursorTile(7);
    public static Tile hoverTile        = new HoverTile(8);
    public static Tile attackTile       = new AttackTile(9);

    public static final int TILE_WIDTH  = 48;                       /*!< Latimea unei dale.*/
    public static final int TILE_HEIGHT = 48;                       /*!< Inaltimea unei dale.*/

    protected BufferedImage img;                                    /*!< Imaginea aferenta tipului de dala.*/
    protected final int id;                                         /*!< Id-ul unic aferent tipului de dala.*/
    protected Boolean collides;

    /*! \fn public Tile(BufferedImage texture, int id)
        \brief Constructorul aferent clasei.

        \param image Imaginea corespunzatoare dalei.
        \param id Id-ul dalei.
     */
    public Tile(BufferedImage image, int idd, Boolean col)
    {
        img = image;
        id = idd;
        collides = col;

        tiles[id] = this;
    }

    /*! \fn public void Update()
        \brief Actualizeaza proprietatile dalei.
     */
    public void Update()
    {

    }

    /*! \fn public void Draw(Graphics g, int x, int y)
        \brief Deseneaza in fereastra dala.

        \param g Contextul grafic in care sa se realizeze desenarea
        \param x Coordonata x in cadrul ferestrei unde sa fie desenata dala
        \param y Coordonata y in cadrul ferestrei unde sa fie desenata dala
     */
    public void Draw(Graphics g, int x, int y)
    {
            /// Desenare dala
        g.drawImage(img, x, y, TILE_WIDTH, TILE_HEIGHT, null);
    }

    /*! \fn public boolean IsSolid()
        \brief Returneaza proprietatea de dala solida (supusa coliziunilor) sau nu.
     */
    public boolean IsSolid()
    {
        return false;
    }

    /*! \fn public int GetId()
        \brief Returneaza id-ul dalei.
     */
    public int GetId()
    {
        return id;
    }
}
