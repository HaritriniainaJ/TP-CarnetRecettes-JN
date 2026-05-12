<?php

namespace App\Http\Controllers;

use App\Models\Recette;
use Illuminate\Http\Request;

class RecetteController extends Controller
{
    // GET /api/recettes
    public function index()
    {
        return response()->json(Recette::all());
    }

    // POST /api/recettes
    public function store(Request $request)
    {
        $validated = $request->validate([
            'nom'            => 'required|string|max:255',
            'ingredients'    => 'required|string',
            'instructions'   => 'required|string',
            'temps_prep_min' => 'required|integer|min:1',
            'portions'       => 'required|integer|min:1',
        ]);

        $recette = Recette::create($validated);
        return response()->json($recette, 201);
    }

    // GET /api/recettes/{id}
    public function show($id)
    {
        $recette = Recette::find($id);
        if (!$recette) {
            return response()->json(['message' => 'Recette non trouvée'], 404);
        }
        return response()->json($recette);
    }

    // PUT /api/recettes/{id}
    public function update(Request $request, $id)
    {
        $recette = Recette::find($id);
        if (!$recette) {
            return response()->json(['message' => 'Recette non trouvée'], 404);
        }

        $validated = $request->validate([
            'nom'            => 'required|string|max:255',
            'ingredients'    => 'required|string',
            'instructions'   => 'required|string',
            'temps_prep_min' => 'required|integer|min:1',
            'portions'       => 'required|integer|min:1',
        ]);

        $recette->update($validated);
        return response()->json($recette);
    }

    // DELETE /api/recettes/{id}
    public function destroy($id)
    {
        $recette = Recette::find($id);
        if (!$recette) {
            return response()->json(['message' => 'Recette non trouvée'], 404);
        }

        $recette->delete();
        return response()->json(['message' => 'Recette supprimée']);
    }
}