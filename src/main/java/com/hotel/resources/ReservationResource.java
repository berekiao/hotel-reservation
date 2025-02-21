package com.hotel.resources;

import com.hotel.entities.Reservation;
import com.hotel.entities.Chambre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

import jakarta.validation.Valid;

@Path("/reservations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResource {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("hotelPU");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // 1️⃣ Récupérer toutes les réservations
    @GET
    @Path("/all")
    public List<Reservation> getAllReservations() {
        EntityManager em = getEntityManager();
        List<Reservation> reservations = em.createQuery("SELECT r FROM Reservation r", Reservation.class)
                .getResultList();
        em.close();
        return reservations;
    }

    // 2️⃣ Récupérer une réservation par ID
    @GET
    @Path("/{id}")
    public Response getReservation(@PathParam("id") Long id) {
        EntityManager em = getEntityManager();
        Reservation reservation = em.find(Reservation.class, id);
        em.close();
        if (reservation == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Réservation non trouvée").build();
        }
        return Response.ok(reservation).build();
    }

    // 3️⃣ Ajouter une réservation
    @POST
    public Response addReservation(@Valid Reservation reservation) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        // Vérifier si la chambre existe
        Chambre chambre = em.find(Chambre.class, reservation.getChambre().getId());
        if (chambre == null) {
            em.getTransaction().rollback();
            em.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Chambre introuvable").build();
        }

        reservation.setChambre(chambre);
        em.persist(reservation);
        em.getTransaction().commit();
        em.close();
        return Response.status(Response.Status.CREATED).entity(reservation).build();
    }

    // 4️⃣ Modifier une réservation
    @PUT
    @Path("/{id}")
    public Response updateReservation(@PathParam("id") Long id, @Valid Reservation updatedReservation) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Reservation reservation = em.find(Reservation.class, id);
        if (reservation == null) {
            em.getTransaction().rollback();
            em.close();
            return Response.status(Response.Status.NOT_FOUND).entity("Réservation non trouvée").build();
        }

        // Vérifier si la nouvelle chambre existe
        Chambre chambre = em.find(Chambre.class, updatedReservation.getChambre().getId());
        if (chambre == null) {
            em.getTransaction().rollback();
            em.close();
            return Response.status(Response.Status.BAD_REQUEST).entity("Chambre introuvable").build();
        }

        reservation.setNomClient(updatedReservation.getNomClient());
        reservation.setDateDebut(updatedReservation.getDateDebut());
        reservation.setDateFin(updatedReservation.getDateFin());
        reservation.setChambre(chambre);

        em.merge(reservation);
        em.getTransaction().commit();
        em.close();
        return Response.ok(reservation).build();
    }

    // 5️⃣ Supprimer une réservation
    @DELETE
    @Path("/{id}")
    public Response deleteReservation(@PathParam("id") Long id) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Reservation reservation = em.find(Reservation.class, id);
        if (reservation == null) {
            em.getTransaction().rollback();
            em.close();
            return Response.status(Response.Status.NOT_FOUND).entity("Réservation non trouvée").build();
        }
        em.remove(reservation);
        em.getTransaction().commit();
        em.close();
        return Response.ok().entity("Réservation supprimée avec succès").build();
    }

    @GET
    @Path("/client/{nom}")
    public List<Reservation> getReservationsByClient(@PathParam("nom") String nomClient) {
        EntityManager em = getEntityManager();
        List<Reservation> reservations = em.createQuery(
                "SELECT r FROM Reservation r WHERE LOWER(r.nomClient) LIKE LOWER(:nom)", Reservation.class)
                .setParameter("nom", "%" + nomClient + "%") // Recherche partielle insensible à la casse
                .getResultList();
        em.close();
        return reservations;
    }

}
