package com.hotel.grpc.service;

import com.hotel.common.dto.ReservationDTO;
import com.hotel.common.service.ReservationService;
import com.hotel.grpc.mapper.GrpcMapper;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

/**
 * gRPC Service implementation for Reservation operations.
 * Note: This is a simplified implementation. The actual proto-generated classes
 * would be used after running protoc compiler.
 */
@GrpcService
@RequiredArgsConstructor
@Slf4j
public class ReservationGrpcService {

    private final ReservationService reservationService;
    private final GrpcMapper grpcMapper;

    public void getAllReservations(Object request, StreamObserver<Object> responseObserver) {
        log.info("gRPC: Getting all reservations");
        try {
            List<ReservationDTO> reservations = reservationService.findAll();
            // Convert to proto and send response
            log.info("gRPC: Found {} reservations", reservations.size());
            responseObserver.onNext(grpcMapper.toReservationListProto(reservations));
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("gRPC: Error getting reservations", e);
            responseObserver.onError(e);
        }
    }

    public void getReservationById(Long id, StreamObserver<Object> responseObserver) {
        log.info("gRPC: Getting reservation by ID: {}", id);
        try {
            ReservationDTO reservation = reservationService.findById(id);
            responseObserver.onNext(grpcMapper.toReservationProto(reservation));
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("gRPC: Error getting reservation", e);
            responseObserver.onError(e);
        }
    }

    public void createReservation(ReservationDTO dto, StreamObserver<Object> responseObserver) {
        log.info("gRPC: Creating reservation for client {} in room {}", dto.getClientId(), dto.getChambreId());
        try {
            ReservationDTO created = reservationService.create(dto);
            responseObserver.onNext(grpcMapper.toReservationProto(created));
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("gRPC: Error creating reservation", e);
            responseObserver.onError(e);
        }
    }

    public void updateReservation(Long id, ReservationDTO dto, StreamObserver<Object> responseObserver) {
        log.info("gRPC: Updating reservation: {}", id);
        try {
            ReservationDTO updated = reservationService.update(id, dto);
            responseObserver.onNext(grpcMapper.toReservationProto(updated));
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("gRPC: Error updating reservation", e);
            responseObserver.onError(e);
        }
    }

    public void deleteReservation(Long id, StreamObserver<Object> responseObserver) {
        log.info("gRPC: Deleting reservation: {}", id);
        try {
            reservationService.delete(id);
            responseObserver.onNext(grpcMapper.toDeleteResponseProto(true, "Réservation supprimée avec succès"));
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("gRPC: Error deleting reservation", e);
            responseObserver.onNext(grpcMapper.toDeleteResponseProto(false, e.getMessage()));
            responseObserver.onCompleted();
        }
    }

    public void getReservationsByClient(Long clientId, StreamObserver<Object> responseObserver) {
        log.info("gRPC: Getting reservations for client: {}", clientId);
        try {
            List<ReservationDTO> reservations = reservationService.findByClientId(clientId);
            responseObserver.onNext(grpcMapper.toReservationListProto(reservations));
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("gRPC: Error getting reservations by client", e);
            responseObserver.onError(e);
        }
    }

    public void streamReservations(Object request, StreamObserver<Object> responseObserver) {
        log.info("gRPC: Streaming all reservations");
        try {
            List<ReservationDTO> reservations = reservationService.findAll();
            for (ReservationDTO reservation : reservations) {
                responseObserver.onNext(grpcMapper.toReservationProto(reservation));
            }
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("gRPC: Error streaming reservations", e);
            responseObserver.onError(e);
        }
    }
}
