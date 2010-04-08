/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.support;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import za.co.jumpingbean.calamari.dal.DBException;

/**
 *
 * @author mark
 */
@Provider
public class WebServiceException implements ExceptionMapper<DBException> {

    @Override
    public Response toResponse(DBException exception) {
      return Response.status(404).entity(exception.getMessage()).type("text/plain").build();
    }

}
