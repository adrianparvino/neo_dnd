# This file is responsible for configuring your application
# and its dependencies with the aid of the Mix.Config module.
#
# This configuration file is loaded before any dependency and
# is restricted to this project.

# General application configuration
use Mix.Config

config :neo_dnd,
  ecto_repos: [NeoDnd.Repo]

# Configures the endpoint
config :neo_dnd, NeoDndWeb.Endpoint,
  url: [host: "localhost"],
  secret_key_base: "x/kKiZV5di98SZpiwiz8dCnY0Q71KOcaSBwNT9dyrDq79jl65tTt2vK/sk38yONd",
  render_errors: [view: NeoDndWeb.ErrorView, accepts: ~w(html json), layout: false],
  pubsub_server: NeoDnd.PubSub,
  live_view: [signing_salt: "KdzO5YLd"]

# Configures Elixir's Logger
config :logger, :console,
  format: "$time $metadata[$level] $message\n",
  metadata: [:request_id]

# Use Jason for JSON parsing in Phoenix
config :phoenix, :json_library, Jason

# Import environment specific config. This must remain at the bottom
# of this file so it overrides the configuration defined above.
import_config "#{Mix.env()}.exs"
