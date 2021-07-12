defmodule NeoDndWeb.BattleChannel do
  use Phoenix.Channel

  def join("battle", _message, socket) do
    characters = NeoDnd.Battle.characters(NeoDnd.Battle)

    {:ok, %{characters: characters}, socket}
  end

  def handle_in("next", %{}, socket) do
    NeoDnd.Battle.next(Dnd.Battle)

    broadcast_from!(socket, "next", %{})
    {:noreply, socket}
  end

  def handle_in("set-characters", payload, socket) do
    NeoDnd.Battle.set_characters(NeoDnd.Battle, payload)

    broadcast_from!(socket, "set-characters", %{cs: payload})
    {:noreply, socket}
  end
end
