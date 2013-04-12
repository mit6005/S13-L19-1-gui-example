package hogwarts;

import static hogwarts.model.House.*;
import hogwarts.model.*;
import hogwarts.model.Castle.SameNameException;

import java.net.URL;

public class Hogwarts {
    
    /** Create castle Hogwarts. */
    public static Castle makeHogwarts() {
        try {
            Castle hogwarts = new Castle();
            
            URL noPicture = Hogwarts.class.getResource("gui/images/no-photo.jpg");
            
            hogwarts.add(new Wizard(hogwarts, Gryffindor, "Harry Potter", Hogwarts.class.getResource("gui/images/harry.jpg")));
            hogwarts.add(new Wizard(hogwarts, Gryffindor, "Hermione Granger", Hogwarts.class.getResource("gui/images/hermione.jpg")));
            hogwarts.add(new Wizard(hogwarts, Gryffindor, "Ron Weasley", noPicture));
            hogwarts.add(new Wizard(hogwarts, Gryffindor, "Albus Dumbledore", noPicture));
            hogwarts.add(new Wizard(hogwarts, Hufflepuff, "Cedric Diggory", noPicture));
            hogwarts.add(new Wizard(hogwarts, Slytherin, "Severus Snape", noPicture));
            
            hogwarts.lookup("Harry Potter").friend(hogwarts.lookup("Hermione Granger"));
            hogwarts.lookup("Harry Potter").friend(hogwarts.lookup("Ron Weasley"));
            hogwarts.lookup("Hermione Granger").friend(hogwarts.lookup("Ron Weasley"));
            hogwarts.lookup("Harry Potter").friend(hogwarts.lookup("Albus Dumbledore"));
            hogwarts.lookup("Severus Snape").friend(hogwarts.lookup("Albus Dumbledore"));
            
            return hogwarts;
        } catch (SameNameException sne) {
            sne.printStackTrace();
            throw new AssertionError("shouldn't happen");
        }
    }
}
