package com.softcell.gonogo.uaaserver.model;

/**
 * Response mock, to abstract a dao query result, or a remote service response
 *
 */

public class Response {

    private boolean ok;

    private Response(boolean result) {
        this.ok = result;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    /**
     * @return constructs a success response
     */
    public static Response ok() {
        return new Response(true);
    }

    /**
     * @return constructs a failure response
     */
    public static Response fail() {
        return new Response(false);
    }

}
