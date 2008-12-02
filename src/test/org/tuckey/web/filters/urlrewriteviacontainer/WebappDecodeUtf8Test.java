package org.tuckey.web.filters.urlrewriteviacontainer;


import org.apache.commons.httpclient.methods.GetMethod;

import javax.servlet.ServletException;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * todo: need to do a few tests
 * <p/>
 * with eocode-using not set (ie, browser encoding used, step down to utf8)
 * with eocode-using set to utf (force always with a specific decoding)
 * with eocode-using not set to null (never decode)
 * accept-encoding header?
 * <p/>
 * <p/>
 * don't decode anything - null
 * browser then utf then default - default browser,utf
 * browser then don't decode - default browser,null
 * always utf - utf
 * <p/>
 * <p/>
 * options: browser (may fail), enc (unlikely fail)
 */
public class WebappDecodeUtf8Test extends ContainerTestBase {

    protected String getApp() {
        return "webapp";
    }

    protected String getConf() {
        return "urlrewrite-decode-utf8.xml";
    }

    public void testSetup() throws IOException {
        super.recordRewriteStatus();
    }


    /**
     * note, had trouble keeping true utf (multi byte) chars as cvs buggers them up!
     */
    public void testTestUtf() throws ServletException, IOException {
        String utfSampleString = "F�tel'ha�volap�k";
        GetMethod method = new GetMethod(getBaseUrl() + "/utf/" + URLEncoder.encode(utfSampleString, "UTF8") + "/");
        method.setRequestHeader("Accept-Encoding", "utf8");
        method.setFollowRedirects(false);
        client.executeMethod(method);
        assertEquals(getBaseUrl() + "/utf-redir/done/", method.getResponseHeader("Location").getValue());
    }

    public void testTestUtfToNull() throws ServletException, IOException {
        String encodedStr = URLEncoder.encode("F�tel'ha�volap�k", "UTF8");
        GetMethod method = new GetMethod(getBaseUrl() + "/utf/" + encodedStr + "/to-null/");
        method.setRequestHeader("Accept-Encoding", "utf8");
        method.setFollowRedirects(false);
        client.executeMethod(method);
        String locationHeader = method.getResponseHeader("Location").getValue();
        assertTrue("should start with '" + getBaseUrl() + "/utf-redir/done/to-null/'" +
                " got '" + locationHeader + "'",
                locationHeader.startsWith(getBaseUrl() + "/utf-redir/done/to-null/"));
    }


}
