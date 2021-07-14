defmodule NeoDndWeb.NotesController do
  use NeoDndWeb, :controller

  def index(conn, %{"id" => "me"}) do
    pid = conn.assigns.session_pid
    notes = NeoDnd.Session.Router.list_notes(pid)

    json(conn, notes)
  end

  def create(conn, %{"id" => "me", "content" => body}) do
    pid = conn.assigns.session_pid
    note = NeoDnd.Session.Router.add_note(pid, body)

    json(conn, note)
  end
end
