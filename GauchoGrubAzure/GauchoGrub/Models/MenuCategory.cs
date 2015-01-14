using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace GauchoGrub.Models
{
    public class MenuCategory
    {
        public int Id { get; set; }

        [Required]
        public String Name { get; set; }
    }
}