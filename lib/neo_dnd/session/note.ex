defmodule NeoDnd.Session.Note do
  use Ecto.Schema

  schema "notes" do
    field :session, :integer
    field :content, :string
  end
end
