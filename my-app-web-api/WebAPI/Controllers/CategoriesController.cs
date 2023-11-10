﻿using AutoMapper;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using WebAPI.data.Entities;
using WebAPI.Data;
using WebAPI.Models.Category;
using Microsoft.EntityFrameworkCore;

namespace WebAPI.Controllers
{
    [Route("api/categories")]
    [ApiController]
    public class CategoriesController(AppEFContext appEFContext, 
                                      IMapper mapper) : ControllerBase
    {
        [HttpGet("list")]
        public async Task<IActionResult> Index()
        {
            var model = await appEFContext.Categories
                .Where(x => x.IsDeleted == false)
                .Select(x => mapper.Map<CategoryItemViewModel>(x))
                .ToListAsync();

            return Ok(model);
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(int id)
        {
            var cat = await appEFContext.Categories
                .Where(x => x.IsDeleted == false)
                .SingleOrDefaultAsync(x => x.Id == id);

            if (cat == null)
                return NotFound();

            var model = mapper.Map<CategoryItemViewModel>(cat);
            return Ok(model);
        }

        [HttpPost("create")]
        public async Task<IActionResult> Create([FromForm] CategoryCreateViewModel model)
        {
            try
            {
                var cat = mapper.Map<CategoryEntity>(model);

                string imageName = String.Empty;
                if (model.Image != null)
                {
                    string exp = Path.GetExtension(model.Image.FileName);
                    imageName = Path.GetRandomFileName() + exp;
                    string dirSaveImage = Path.Combine(Directory.GetCurrentDirectory(), "images", imageName);
                    using (var stream = System.IO.File.Create(dirSaveImage))
                    {
                        await model.Image.CopyToAsync(stream);
                    }
                }
                cat.Image = imageName;

                await appEFContext.Categories.AddAsync(cat);
                await appEFContext.SaveChangesAsync();
                return Ok(mapper.Map<CategoryItemViewModel>(cat));
            }
            catch (Exception ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }
        [HttpPut("update")]
        public async Task<IActionResult> Put([FromBody] CategoryUpdateViewModel model)
        {
            try
            {
                var cat = await appEFContext.Categories
                                    .Where(x => x.IsDeleted == false)
                                    .SingleOrDefaultAsync(x => x.Id == model.Id);

                if (cat == null)
                    return NotFound();

                cat.Name = model.Name;
                cat.Description = model.Description;
                cat.Image = model.Image;
                await appEFContext.SaveChangesAsync();

                return Ok(mapper.Map<CategoryItemViewModel>(cat));
            }
            catch (Exception ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            try
            {
                var cat = await appEFContext.Categories
                .Where(x => x.IsDeleted == false)
                .SingleOrDefaultAsync(x => x.Id == id);

                if (cat == null)
                    return NotFound();

                cat.IsDeleted = true;
                await appEFContext.SaveChangesAsync();

                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }
    }
}
