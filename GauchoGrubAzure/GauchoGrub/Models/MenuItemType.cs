﻿using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace GauchoGrub.Models
{
    /*
     * MenuItemType - model for representing food item types.
     * Type examples: Regular, Vegetarian, Vegan
     */
    [DataContract]
    public class MenuItemType
    {
        public int Id { get; set; }

        [Required]
        [DataMember]
        [Index(IsUnique = true)] // Extra duplication prevention
        [MaxLength(450)]         // Unique constraint won't work without length limitation
        public String Name { get; set; }
    }
}