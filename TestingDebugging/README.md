# Testing & Debugging
---
<img width="975" height="708" alt="image" src="https://github.com/user-attachments/assets/eadd8333-0f50-423d-a01b-96184a6c20e6" />
<img width="995" height="965" alt="image" src="https://github.com/user-attachments/assets/4583c463-7c24-4361-9945-3501898904bd" />

## SalesItemTest
```java
import static org.junit.Assert.*;
import org.junit.Test;

public class SalesItemTest {

    @Test
    public void testInitialization() {
        SalesItem item = new SalesItem("Laptop", 15000000);

        assertEquals("Laptop", item.getName());
        assertEquals(15000000, item.getPrice());
        assertEquals(0, item.getComments().size());
    }

    @Test
    public void testAddComment() {
        SalesItem item = new SalesItem("Monitor", 2000000);

        boolean result = item.addComment("Andi", "Bagus sekali", 5);

        assertTrue(result);
        assertEquals(1, item.getComments().size());

        Comment c = item.getComments().get(0);
        assertEquals("Andi", c.getAuthor());
        assertEquals("Bagus sekali", c.getText());
        assertEquals(5, c.getRating());
    }

    @Test
    public void testIllegalRatingLow() {
        SalesItem item = new SalesItem("Keyboard", 300000);

        boolean result = item.addComment("Budi", "Kurang bagus", 0);

        assertFalse(result);
        assertEquals(0, item.getComments().size());
    }

    @Test
    public void testIllegalRatingHigh() {
        SalesItem item = new SalesItem("Mouse", 150000);

        boolean result = item.addComment("Sari", "Mahal", 6);

        assertFalse(result);
        assertEquals(0, item.getComments().size());
    }

    @Test
    public void testRatingBoundaries() {
        SalesItem item = new SalesItem("Speaker", 500000);

        boolean low = item.addComment("Andi", "Lumayan", 1);
        boolean high = item.addComment("Budi", "Mantap", 5);

        assertTrue(low);
        assertTrue(high);
        assertEquals(2, item.getComments().size());
    }

    @Test
    public void testDuplicateAuthor() {
        SalesItem item = new SalesItem("Headset", 750000);

        assertTrue(item.addComment("Andi", "Suaranya oke", 4));
        assertFalse(item.addComment("andi", "Coba komentar kedua", 3)); // case-insensitive

        assertEquals(1, item.getComments().size());
    }
}
```

## SalesItem
```java
import java.util.ArrayList;

public class SalesItem {
    private String name;
    private int price;
    private ArrayList<Comment> comments;

    public SalesItem(String name, int price) {
        this.name = name;
        this.price = price;
        this.comments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    /**
     * Menambahkan komentar baru.
     * - Return false jika rating <1 atau >5
     * - Return false jika author sudah pernah berkomentar (case-insensitive)
     * - Return true jika komentar berhasil ditambahkan
     */
    public boolean addComment(String author, String text, int rating) {
        if (rating < 1 || rating > 5) {
            return false;
        }

        for (Comment existing : comments) {
            if (existing.getAuthor().equalsIgnoreCase(author)) {
                return false;
            }
        }

        Comment newComment = new Comment(author, text, rating);
        comments.add(newComment);
        return true;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    /**
     * Debugging untuk BlueJ.
     * Menampilkan seluruh data item dan daftar comment.
     */
    public void debugPrint() {
        System.out.println("=== DEBUG SALES ITEM ===");
        System.out.println("Name  : " + name);
        System.out.println("Price : " + price);
        System.out.println("Comments (" + comments.size() + "):");

        for (Comment c : comments) {
            System.out.println("- " + c.toString());
        }

        System.out.println("=========================");
    }
}
```

## Comment.java
```java
public class Comment {
    private String author;
    private String text;
    private int rating;

    public Comment(String author, String text, int rating) {
        this.author = author;
        this.text = text;
        this.rating = rating;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
    }

    public String toString() {
        return author + " (" + rating + "/5): " + text;
    }

    /**
     * Debugging method untuk BlueJ.
     * Menampilkan detail comment.
     */
    public void debugPrint() {
        System.out.println("=== DEBUG COMMENT ===");
        System.out.println("Author : " + author);
        System.out.println("Rating : " + rating);
        System.out.println("Text   : " + text);
        System.out.println("=====================");
    }
}
```





