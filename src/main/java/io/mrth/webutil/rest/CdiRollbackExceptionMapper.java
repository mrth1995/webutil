package io.mrth.webutil.rest;

import javax.ws.rs.ext.Provider;

@Provider
public class CdiRollbackExceptionMapper extends DefaultExceptionMapper<javax.transaction.RollbackException> {

}
