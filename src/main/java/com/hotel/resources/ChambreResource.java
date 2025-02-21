package com.hotel.resources;

import com.hotel.entities.Chambre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import jakarta.validation.Valid;

@Path("/chambres")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChambreResource {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("hotelPU");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @GET
    @Path("/all")
    public List<Chambre> getAllChambres() {
        EntityManager em = getEntityManager();
        List<Chambre> chambres = em.createQuery("SELECT c FROM Chambre c", Chambre.class).getResultList();
        em.close();
        return chambres;
    }

    @GET
    @Path("/{id}")
    public Response getChambre(@PathParam("id") Long id) {
        EntityManager em = getEntityManager();
        Chambre chambre = em.find(Chambre.class, id);
        em.close();
        if (chambre == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Chambre non trouvée").build();
        }
        return Response.ok(chambre).build();
    }

    @POST
    public Response addChambre(@Valid Chambre chambre) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(chambre);
        em.getTransaction().commit();
        em.close();
        return Response.status(Response.Status.CREATED).entity(chambre).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateChambre(@PathParam("id") Long id, @Valid Chambre updatedChambre) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Chambre chambre = em.find(Chambre.class, id);
        if (chambre == null) {
            em.getTransaction().rollback();
            em.close();
            return Response.status(Response.Status.NOT_FOUND).entity("Chambre non trouvée").build();
        }
        chambre.setNumero(updatedChambre.getNumero());
        chambre.setNbPlaces(updatedChambre.getNbPlaces());
        chambre.setPrix(updatedChambre.getPrix());
        chambre.setCategorie(updatedChambre.getCategorie());

        em.merge(chambre);
        em.getTransaction().commit();
        em.close();
        return Response.ok(chambre).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteChambre(@PathParam("id") Long id) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Chambre chambre = em.find(Chambre.class, id);
        if (chambre == null) {
            em.getTransaction().rollback();
            em.close();
            return Response.status(Response.Status.NOT_FOUND).entity("Chambre non trouvée").build();
        }
        em.remove(chambre);
        em.getTransaction().commit();
        em.close();
        return Response.ok().entity("Chambre supprimée avec succès").build();
    }

    @GET
    @Path("/disponibles")
    public List<Chambre> getChambresDisponibles() {
        EntityManager em = getEntityManager();
        List<Chambre> chambres = em.createQuery(
                "SELECT c FROM Chambre c WHERE c.id NOT IN (" +
                        "SELECT r.chambre.id FROM Reservation r WHERE r.dateFin >= CURRENT_DATE)",
                Chambre.class).getResultList();
        em.close();
        return chambres;
    }

}
