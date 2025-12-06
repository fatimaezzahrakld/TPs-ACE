package ma.projet.grpc.controllers;

import io.grpc.stub.StreamObserver;
import ma.projet.grpc.services.CompteService;
import ma.projet.grpc.stubs.CompteRequest;
import ma.projet.grpc.stubs.CompteServiceGrpc;
import ma.projet.grpc.stubs.GetAllComptesRequest;
import ma.projet.grpc.stubs.GetAllComptesResponse;
import ma.projet.grpc.stubs.GetCompteByIdRequest;
import ma.projet.grpc.stubs.GetCompteByIdResponse;
import ma.projet.grpc.stubs.GetTotalSoldeRequest;
import ma.projet.grpc.stubs.GetTotalSoldeResponse;
import ma.projet.grpc.stubs.SaveCompteRequest;
import ma.projet.grpc.stubs.SaveCompteResponse;
import ma.projet.grpc.stubs.SoldeStats;
import ma.projet.grpc.stubs.TypeCompte;
import ma.projet.grpc.stubs.Compte;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class CompteServiceImpl extends CompteServiceGrpc.CompteServiceImplBase {
    private final CompteService compteService;

    public CompteServiceImpl(CompteService compteService) {
        this.compteService = compteService;
    }

    @Override
    public void allComptes(GetAllComptesRequest request, StreamObserver<GetAllComptesResponse> responseObserver) {
        List<Compte> comptes = compteService.findAllComptes().stream()
                .map(entity -> Compte.newBuilder()
                        .setId(entity.getId())
                        .setSolde(entity.getSolde())
                        .setDateCreation(entity.getDateCreation())
                        .setType(TypeCompte.valueOf(entity.getType()))
                        .build())
                .collect(Collectors.toList());

        responseObserver.onNext(GetAllComptesResponse.newBuilder().addAllComptes(comptes).build());
        responseObserver.onCompleted();
    }

    @Override
    public void compteById(GetCompteByIdRequest request, StreamObserver<GetCompteByIdResponse> responseObserver) {
        var entity = compteService.findCompteById(request.getId());
        if (entity == null) {
            responseObserver.onError(io.grpc.Status.NOT_FOUND.withDescription("Compte non trouvé: " + request.getId()).asRuntimeException());
            return;
        }

        Compte grpcCompte = Compte.newBuilder()
                .setId(entity.getId())
                .setSolde(entity.getSolde())
                .setDateCreation(entity.getDateCreation())
                .setType(TypeCompte.valueOf(entity.getType()))
                .build();

        responseObserver.onNext(GetCompteByIdResponse.newBuilder().setCompte(grpcCompte).build());
        responseObserver.onCompleted();
    }

    @Override
    public void totalSolde(GetTotalSoldeRequest request, StreamObserver<GetTotalSoldeResponse> responseObserver) {
        var entities = compteService.findAllComptes();
        int count = entities.size();
        float sum = 0f;
        for (var e : entities) sum += e.getSolde();
        float average = count > 0 ? sum / count : 0f;

        SoldeStats stats = SoldeStats.newBuilder()
                .setCount(count)
                .setSum(sum)
                .setAverage(average)
                .build();

        responseObserver.onNext(GetTotalSoldeResponse.newBuilder().setStats(stats).build());
        responseObserver.onCompleted();
    }

    @Override
    public void saveCompte(SaveCompteRequest request, StreamObserver<SaveCompteResponse> responseObserver) {
        CompteRequest compteReq = request.getCompte();

        ma.projet.grpc.entities.Compte entity = new ma.projet.grpc.entities.Compte();
        entity.setSolde(compteReq.getSolde());
        entity.setDateCreation(compteReq.getDateCreation());
        entity.setType(compteReq.getType().name());

        var saved = compteService.saveCompte(entity);

        Compte grpcCompte = Compte.newBuilder()
                .setId(saved.getId())
                .setSolde(saved.getSolde())
                .setDateCreation(saved.getDateCreation())
                .setType(TypeCompte.valueOf(saved.getType()))
                .build();

        responseObserver.onNext(SaveCompteResponse.newBuilder().setCompte(grpcCompte).build());
        responseObserver.onCompleted();
    }
}
