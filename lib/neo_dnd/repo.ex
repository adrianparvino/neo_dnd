defmodule NeoDnd.Repo do
  use Ecto.Repo,
    otp_app: :neo_dnd,
    adapter: Ecto.Adapters.Postgres
end
