from concurrent import futures
import logging
import grpc
import population_pb2_grpc
import population_pb2
class PopulationDataService(population_pb2_grpc.PopulationDataServiceServicer):

      def GetPopulation(self, request, context):
          return population_pb2.PopulationData


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    population_pb2_grpc.add_PopulationDataServiceServicer_to_server(PopulationDataService(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    server.wait_for_termination()


if __name__ == '__main__':
    logging.basicConfig()
    serve()
