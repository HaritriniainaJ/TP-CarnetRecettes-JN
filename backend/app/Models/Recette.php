<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Recette extends Model
{
    protected $fillable = [
        'nom',
        'ingredients',
        'instructions',
        'temps_prep_min',
        'portions',
    ];
}