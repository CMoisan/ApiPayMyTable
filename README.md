# ApiPayMyTable
API permettant de créer et gérer des short_url

POST /short_url permet de créer et persister une short URL
GET /short_url/:short_url_id permet de récupérer une short URL
PUT /short_url/:short_url_id permet de modifier une short URL (C'est à dire pouvoir remplacer l'ancienne Url tout en conservant le Short Url)
DELETE /short_url/:short_url_id permet de supprimer une short URL

Choix techniques :

Spring Data

Mongodb via atlas 
