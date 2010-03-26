import com.google.gdata.data.DateTime;
import com.google.gdata.data.Link;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import java.io.PrintStream;
import java.util.Set;

class GDTester
{
  public static void main(String[] args)
    throws Exception
  {
    GoogleDocBrowser gdb = new GoogleDocBrowser();

    if (gdb.login() != 0) //Put prompt here
    {
      System.out.println(gdb.popErrMSG());
    }

    gdb.setActiveFeed(GoogleDocBrowser.FeedType.PRIVATE);

    if (gdb.refreshFileList() != 0)
    {
      System.out.println(gdb.popErrMSG());
    }

    showAllDocs(gdb.getActiveDocumentListFeed());
  }

  public static void showAllDocs(DocumentListFeed feed)
  {
    for (DocumentListEntry entry : feed.getEntries())
      printDocumentEntry(entry);
  }

  public static void printDocumentEntry(DocumentListEntry doc)
  {
    String resourceId = doc.getResourceId();
    String docType = resourceId.substring(0, resourceId.lastIndexOf(':'));

    System.out.println("'" + doc.getTitle().getPlainText() + "' (" + docType + ")");
    System.out.println("  link to Google Docs: " + doc.getHtmlLink().getHref());
    System.out.println("  resource id: " + resourceId);

    if (!doc.getFolders().isEmpty()) {
      System.out.println("  in folder: " + doc.getFolders());
    }

    System.out.println("  last updated: " + doc.getUpdated().toString());
    System.out.println("  viewed by user? " + doc.isViewed());
    System.out.println("  writersCanInvite? " + doc.isWritersCanInvite().toString());
    System.out.println("  hidden? " + doc.isHidden());
    System.out.println("  starrred? " + doc.isStarred());
    System.out.println();
  }
}