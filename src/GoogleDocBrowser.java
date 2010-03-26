import com.google.gdata.client.docs.DocsService;
import com.google.gdata.data.docs.DocumentListFeed;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

class GoogleDocBrowser
{
  private static final String _coName = "JPCUnlimited";
  private static final String _appName = "JPasswordKeeper";
  private static final String _version = "1.0";
  private FeedType activeFeed;
  private DocsService client;
  private boolean isLogedIn;
  private String chDir;
  private String errMSG;
  private String activeUser;
  private HashMap<FeedType, DocumentListFeed> mapFeedToDocumentListFeed;
  private HashMap<FeedType, URL> mapFeedToUri;
  private Exception lastException;
  private static final String privateStr = "http://docs.google.com/feeds/default/private/full/";
  private static final String publicStr = "http://docs.google.com/feeds/default/public/full/";

  public GoogleDocBrowser()
    throws Exception
  {
    client = new DocsService("JPCUnlimited-PasswordKeeper-1.0");

    isLogedIn = false;

    mapFeedToDocumentListFeed = new HashMap<FeedType, DocumentListFeed>();

    mapFeedToUri = new HashMap<FeedType, URL>();
    mapFeedToUri.put(FeedType.PRIVATE, new URL("http://docs.google.com/feeds/default/private/full/"));
    mapFeedToUri.put(FeedType.PUBLIC, new URL("http://docs.google.com/feeds/default/public/full/"));

    activeFeed = null;

    lastException = null;

    reset();
  }

  public int login(String uname, String password)
  {
    if (isLogedIn)
    {
      errMSG = (activeUser + " is already logged in");

      return 3;
    }

    try
    {
      client.setUserCredentials(uname, password);

      activeUser = uname;

      isLogedIn = true;

      chDir = "/";

      return 0;
    }
    catch (Exception e)
    {
      errMSG = "An error occured while attempting to log in to the google server";

      lastException = e;
    }
    return 1;
  }

  public boolean logout()
  {
    if (isLogedIn)
    {
      reset();
    }
    else
    {
      errMSG = (activeUser + " is already logged in");
    }
    return !isLogedIn;
  }

  public int refreshFileList()
  {
    if (!isLogedIn)
    {
      errMSG = "Not logged in";

      return 3;
    }

    if (activeFeed == null)
    {
      errMSG = "No feed selected";

      return 3;
    }

    try
    {
      DocumentListFeed dlf = client.getFeed(mapFeedToUri.get(activeFeed), DocumentListFeed.class);

      mapFeedToDocumentListFeed.put(activeFeed, dlf);

      if ((dlf == null) || (dlf.getEntries().size() == 0))
      {
        errMSG = "There are no entries";

        return 2;
      }

      return 0;
    }
    catch (Exception e)
    {
      errMSG = "Failed to contact the feed server";

      lastException = e;
    }
    return 1;
  }

  public void setActiveFeed(FeedType feedType)
  {
    activeFeed = feedType;
  }

  public String popErrMSG()
  {
    String errMSG_ = getErrMSG();

    errMSG = null;

    return errMSG_;
  }

  public String getErrMSG()
  {
    return errMSG;
  }

  public boolean isLogedIn()
  {
    return isLogedIn;
  }

  public Exception getLastException()
  {
    return lastException;
  }

  public DocumentListFeed getActiveDocumentListFeed()
  {
    if (activeFeed == null)
    {
      return null;
    }
    return mapFeedToDocumentListFeed.get(activeFeed);
  }

  private void reset()
  {
    mapFeedToDocumentListFeed.put(FeedType.PRIVATE, null);
    mapFeedToDocumentListFeed.put(FeedType.PUBLIC, null);
    isLogedIn = false;
  }

  public static enum FeedType
  {
    PRIVATE, PUBLIC;
  }
}