defmodule NeoDnd.Repo.Migrations.CreateNotes do
  use Ecto.Migration

  def change do
    create table(:notes) do
      add :session, :integer
      add :content, :string
    end
  end
end
