package org.projectpost.pages;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class CSSEndpoint extends RouterNanoHTTPD.DefaultHandler {
    @Override
    public String getText() {
        return "";
    }

    @Override
    public NanoHTTPD.Response.IStatus getStatus() {
        return NanoHTTPD.Response.Status.OK;
    }

    @Override
    public String getMimeType() {
        return "text/css";
    }

    @Override
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        String uri = session.getUri();
        if (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }

        String resource = uri.substring(uri.lastIndexOf("/") + 1);
        resource = resource.replaceAll("\\.\\.", "");

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Files.copy(Paths.get("css", resource), baos);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/css", bais, baos.size());
        } catch (FileNotFoundException e) {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "404 not found");
        } catch (IOException e) {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "internal server error");
        }
    }
}
