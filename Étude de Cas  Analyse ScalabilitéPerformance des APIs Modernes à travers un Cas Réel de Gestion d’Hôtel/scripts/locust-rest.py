"""
Locust load testing script for REST API
Run: locust -f locust-rest.py --host=http://localhost:8080
"""

from locust import HttpUser, task, between, events
import random
import json
from datetime import datetime, timedelta


class ReservationUser(HttpUser):
    """Simulates a user making hotel reservation operations."""
    
    wait_time = between(1, 3)  # Wait 1-3 seconds between tasks
    
    def on_start(self):
        """Initialize user session."""
        self.client_ids = list(range(1, 11))
        self.chambre_ids = list(range(1, 11))
        self.created_reservation_ids = []
    
    @task(5)
    def get_all_reservations(self):
        """GET all reservations - most common operation."""
        with self.client.get("/api/reservations", catch_response=True) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure(f"Status: {response.status_code}")
    
    @task(3)
    def get_reservation_by_id(self):
        """GET single reservation by ID."""
        reservation_id = random.randint(1, 8)
        with self.client.get(f"/api/reservations/{reservation_id}", catch_response=True) as response:
            if response.status_code in [200, 404]:
                response.success()
            else:
                response.failure(f"Status: {response.status_code}")
    
    @task(2)
    def create_reservation(self):
        """POST create new reservation."""
        start_date = datetime.now() + timedelta(days=random.randint(1, 30))
        end_date = start_date + timedelta(days=random.randint(1, 7))
        
        payload = {
            "clientId": random.choice(self.client_ids),
            "chambreId": random.choice(self.chambre_ids),
            "dateDebut": start_date.strftime("%Y-%m-%d"),
            "dateFin": end_date.strftime("%Y-%m-%d"),
            "preferences": f"Locust test - {datetime.now().isoformat()}",
            "nombrePersonnes": random.randint(1, 4),
            "commentaires": "Created by Locust load test"
        }
        
        headers = {"Content-Type": "application/json"}
        
        with self.client.post("/api/reservations", 
                              data=json.dumps(payload), 
                              headers=headers,
                              catch_response=True) as response:
            if response.status_code == 201:
                try:
                    data = response.json()
                    self.created_reservation_ids.append(data.get("id"))
                except:
                    pass
                response.success()
            elif response.status_code == 400:
                # Business validation error (e.g., room not available)
                response.success()
            else:
                response.failure(f"Status: {response.status_code}")
    
    @task(2)
    def get_reservations_by_client(self):
        """GET reservations for a specific client."""
        client_id = random.choice(self.client_ids)
        with self.client.get(f"/api/reservations/client/{client_id}", catch_response=True) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure(f"Status: {response.status_code}")
    
    @task(2)
    def get_available_rooms(self):
        """GET all available rooms."""
        with self.client.get("/api/chambres/available", catch_response=True) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure(f"Status: {response.status_code}")
    
    @task(2)
    def get_all_clients(self):
        """GET all clients."""
        with self.client.get("/api/clients", catch_response=True) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure(f"Status: {response.status_code}")
    
    @task(1)
    def update_reservation(self):
        """PUT update an existing reservation."""
        if not self.created_reservation_ids:
            return
        
        reservation_id = random.choice(self.created_reservation_ids)
        start_date = datetime.now() + timedelta(days=random.randint(1, 30))
        end_date = start_date + timedelta(days=random.randint(1, 7))
        
        payload = {
            "clientId": random.choice(self.client_ids),
            "chambreId": random.choice(self.chambre_ids),
            "dateDebut": start_date.strftime("%Y-%m-%d"),
            "dateFin": end_date.strftime("%Y-%m-%d"),
            "preferences": f"Updated by Locust - {datetime.now().isoformat()}",
            "nombrePersonnes": random.randint(1, 4)
        }
        
        headers = {"Content-Type": "application/json"}
        
        with self.client.put(f"/api/reservations/{reservation_id}",
                             data=json.dumps(payload),
                             headers=headers,
                             catch_response=True) as response:
            if response.status_code in [200, 400, 404]:
                response.success()
            else:
                response.failure(f"Status: {response.status_code}")
    
    @task(1)
    def delete_reservation(self):
        """DELETE a reservation."""
        if not self.created_reservation_ids:
            return
        
        reservation_id = self.created_reservation_ids.pop() if self.created_reservation_ids else None
        if not reservation_id:
            return
        
        with self.client.delete(f"/api/reservations/{reservation_id}", catch_response=True) as response:
            if response.status_code in [204, 404]:
                response.success()
            else:
                response.failure(f"Status: {response.status_code}")
    
    @task(1)
    def check_room_availability(self):
        """GET check room availability for dates."""
        chambre_id = random.choice(self.chambre_ids)
        start_date = datetime.now() + timedelta(days=random.randint(1, 30))
        end_date = start_date + timedelta(days=random.randint(1, 7))
        
        params = {
            "chambreId": chambre_id,
            "dateDebut": start_date.strftime("%Y-%m-%d"),
            "dateFin": end_date.strftime("%Y-%m-%d")
        }
        
        with self.client.get("/api/reservations/availability", 
                            params=params,
                            catch_response=True) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure(f"Status: {response.status_code}")


class AdminUser(HttpUser):
    """Simulates an admin user with different behavior."""
    
    wait_time = between(2, 5)
    weight = 1  # Less common than regular users
    
    @task(3)
    def get_all_reservations(self):
        """GET all reservations."""
        self.client.get("/api/reservations")
    
    @task(2)
    def get_all_clients(self):
        """GET all clients."""
        self.client.get("/api/clients")
    
    @task(2)
    def get_all_rooms(self):
        """GET all rooms."""
        self.client.get("/api/chambres")
    
    @task(1)
    def get_reservations_by_status(self):
        """GET reservations by status."""
        status = random.choice(["EN_ATTENTE", "CONFIRMEE", "TERMINEE"])
        self.client.get(f"/api/reservations/status/{status}")


# Custom event handlers for reporting
@events.request.add_listener
def on_request(request_type, name, response_time, response_length, exception, **kwargs):
    """Log each request for debugging."""
    if exception:
        print(f"Request failed: {name} - {exception}")


@events.test_stop.add_listener
def on_test_stop(environment, **kwargs):
    """Print summary when test stops."""
    print("\n" + "=" * 50)
    print("Load Test Completed")
    print("=" * 50)
