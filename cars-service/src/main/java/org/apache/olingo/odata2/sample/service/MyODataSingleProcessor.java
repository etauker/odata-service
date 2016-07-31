package org.apache.olingo.odata2.sample.service;

import static org.apache.olingo.odata2.sample.service.MyEdmProvider.ENTITY_SET_NAME_CARS;
import static org.apache.olingo.odata2.sample.service.MyEdmProvider.ENTITY_SET_NAME_MANUFACTURERS;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmLiteralKind;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmSimpleType;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties.ODataEntityProviderPropertiesBuilder;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.exception.ODataNotFoundException;
import org.apache.olingo.odata2.api.exception.ODataNotImplementedException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.api.uri.KeyPredicate;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityUriInfo;

public class MyODataSingleProcessor extends ODataSingleProcessor {
  
    private final DataStore dataStore;

    public MyODataSingleProcessor() {
        dataStore = new DataStore();
    }

    @Override
    public ODataResponse readEntitySet(GetEntitySetUriInfo uriInfo, String contentType) throws ODataException {

        EdmEntitySet entitySet;

        if (uriInfo.getNavigationSegments().size() == 0) {
        entitySet = uriInfo.getStartEntitySet();

        if (ENTITY_SET_NAME_CARS.equals(entitySet.getName())) {
            return EntityProvider.writeFeed(contentType, entitySet, dataStore.getCars(), EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
        } else if (ENTITY_SET_NAME_MANUFACTURERS.equals(entitySet.getName())) {
            return EntityProvider.writeFeed(contentType, entitySet, dataStore.getManufacturers(), EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
        }

        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

        } else if (uriInfo.getNavigationSegments().size() == 1) {
            //navigation first level, simplified example for illustration purposes only
            entitySet = uriInfo.getTargetEntitySet();

            if (ENTITY_SET_NAME_CARS.equals(entitySet.getName())) {
                int manufacturerKey = getKeyValue(uriInfo.getKeyPredicates().get(0));

                List<Map<String, Object>> cars = new ArrayList<Map<String, Object>>();
                cars.addAll(dataStore.getCarsFor(manufacturerKey));

                return EntityProvider.writeFeed(contentType, entitySet, cars, EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
            }

            throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
        }

        throw new ODataNotImplementedException();
    }

    @Override
    public ODataResponse readEntity(GetEntityUriInfo uriInfo, String contentType) throws ODataException {

        if (uriInfo.getNavigationSegments().size() == 0) {
            EdmEntitySet entitySet = uriInfo.getStartEntitySet();

            if (ENTITY_SET_NAME_CARS.equals(entitySet.getName())) {
                int id = getKeyValue(uriInfo.getKeyPredicates().get(0));
                Map<String, Object> data = dataStore.getCar(id);

                if (data != null) {
                    URI serviceRoot = getContext().getPathInfo().getServiceRoot();
                    ODataEntityProviderPropertiesBuilder propertiesBuilder = EntityProviderWriteProperties.serviceRoot(serviceRoot);

                    return EntityProvider.writeEntry(contentType, entitySet, data, propertiesBuilder.build());
                }
            } else if (ENTITY_SET_NAME_MANUFACTURERS.equals(entitySet.getName())) {
                int id = getKeyValue(uriInfo.getKeyPredicates().get(0));
                Map<String, Object> data = dataStore.getManufacturer(id);

                if (data != null) {
                    URI serviceRoot = getContext().getPathInfo().getServiceRoot();
                    ODataEntityProviderPropertiesBuilder propertiesBuilder = EntityProviderWriteProperties.serviceRoot(serviceRoot);

                    return EntityProvider.writeEntry(contentType, entitySet, data, propertiesBuilder.build());
                }
            }
            throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

        } else if (uriInfo.getNavigationSegments().size() == 1) {
            //navigation first level, simplified example for illustration purposes only
            EdmEntitySet entitySet = uriInfo.getTargetEntitySet();

            Map<String, Object> data = null;

            if (ENTITY_SET_NAME_MANUFACTURERS.equals(entitySet.getName())) {
                int carKey = getKeyValue(uriInfo.getKeyPredicates().get(0));
                data = dataStore.getManufacturerFor(carKey);
            }

            if(data != null) {
                return EntityProvider.writeEntry(contentType, uriInfo.getTargetEntitySet(), 
                data, EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
            }

            throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
        }

        throw new ODataNotImplementedException();
    }

    // @Override
    // public ODataResponse createEntity(PostUriInfo uriInfo, InputStream content, 
    //     String requestContentType, String contentType) throws ODataException {
    //     //No support for creating and linking a new entry
    //     if (uriInfo.getNavigationSegments().size() > 0) {
    //         throw new ODataNotImplementedException();
    //     }

    //     //No support for media resources
    //     if (uriInfo.getStartEntitySet().getEntityType().hasStream()) {
    //         throw new ODataNotImplementedException();
    //     }

    //     EntityProviderReadProperties properties = EntityProviderReadProperties.init().mergeSemantic(false).build();

    //     ODataEntry entry = EntityProvider.readEntry(requestContentType, uriInfo.getStartEntitySet(), content, properties);
    //     //if something goes wrong in deserialization this is managed via the ExceptionMapper
    //     //no need for an application to do exception handling here an convert the exceptions in HTTP exceptions

    //     Map<String, Object> data = entry.getProperties();
    //     //now one can use the data to create the entry in the backend ...
    //     //retrieve the key value after creation, if the key is generated by the server

    //     //update the data accordingly
    //     data.put("Id", Integer.valueOf(887788675));

    //     //serialize the entry, Location header is set by OData Library
    //     return EntityProvider.writeEntry(contentType, uriInfo.getStartEntitySet(), entry.getProperties(), EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
    // }

    // @Override
    // public ODataResponse updateEntity(PutMergePatchUriInfo uriInfo, InputStream content, String requestContentType, boolean merge, String contentType) throws ODataException {
    //     EntityProviderReadProperties properties = EntityProviderReadProperties.init().mergeSemantic(false).build();

    //     ODataEntry entry = EntityProvider.readEntry(requestContentType, uriInfo.getTargetEntitySet(), content, properties);
    //     //if something goes wrong in deserialization this is managed via the ExceptionMapper,
    //     //no need for an application to do exception handling here an convert the exceptions in HTTP exceptions

    //     Map<String, Object> data = entry.getProperties();

    //     if ("Cars".equals(uriInfo.getTargetEntitySet().getName())) {
    //         int key = getKeyValue(uriInfo.getKeyPredicates().get(0));

    //         //if there is no entry with this key available, one should return "404 Not Found"
    //         //return ODataResponse.status(HttpStatusCodes.NOT_FOUND).build();

    //         //now one can use the data to create the entry in the backend ...
    //         String model = (String) data.get("Model");
    //         //...
    //     } else if ("Manufacturers".equals(uriInfo.getTargetEntitySet().getName())) {
    //         int key = getKeyValue(uriInfo.getKeyPredicates().get(0));
    //         //now one can use the data to create the entry in the backend ...
    //     }

    //     //we can return Status Code 204 No Content because the URI Parsing already guarantees that
    //     //a) only valid URIs are dispatched (also checked against the metadata)
    //     //b) 404 Not Found is already returned above, when the entry does not exist 
    //     return ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
    // }

    // @Override
    // public ODataResponse deleteEntity(DeleteUriInfo uriInfo, String contentType) throws ODataException {
    //     if ("Cars".equals(uriInfo.getTargetEntitySet().getName())) {
    //         int key = getKeyValue(uriInfo.getKeyPredicates().get(0));

    //         //if there is no entry with this key available, one should return "404 Not Found"
    //         //return ODataResponse.status(HttpStatusCodes.NOT_FOUND).build();

    //         //now one can delete the entry with this particular key in the backend...

    //     } else if ("Manufacturers".equals(uriInfo.getTargetEntitySet().getName())) {
    //         int key = getKeyValue(uriInfo.getKeyPredicates().get(0));
    //         //now one can delete the entry with this particular key in the backend...
    //     }

    //     //we can return Status Code 204 No Content because the URI Parsing already guarantees that
    //     //a) only valid URIs are dispatched (also checked against the metadata)
    //     //b) 404 Not Found is already returned above, when the entry does not exist 
    //     return ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
    // }
  
    private int getKeyValue(KeyPredicate key) throws ODataException {
        EdmProperty property = key.getProperty();
        EdmSimpleType type = (EdmSimpleType) property.getType();
        return type.valueOfString(key.getLiteral(), EdmLiteralKind.DEFAULT, property.getFacets(), Integer.class);
    }
}
